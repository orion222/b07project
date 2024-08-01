package com.example.b07demosummer2024.utilities;

import com.example.b07demosummer2024.R;
import com.example.b07demosummer2024.models.Item;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class PdfCreator {
    private int pageWidth = 595;
    private int pageHeight = 842;
    private float curY;
    private float textX;
    private float marginX = 80;

    private Context context;
    private PdfDocument.PageInfo pageInfo;
    private PdfDocument.Page page;
    private Canvas canvas;
    private Paint paint;
    private Paint subtitlePaint;
    private Paint titlePaint;

    private int imageHeight = 100;
    private int imageWidth = 100;
    private float margin = 50;
    private float padding = 20;
    private float lineSpacing = 5;
    private int imageYMargin = 40;

    private float textWidth;
    private float lineHeight;
    private float subtitleLineHeight;
    private float titleLineHeight;


    public void createPdf(Context context, List<Item> items, boolean reducedInfoMode) {
        // Create a new PdfDocument
        this.context = context;

        PdfDocument pdfDocument = new PdfDocument();

        // Create a page description
        pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();

        // Start a page
        page = pdfDocument.startPage(pageInfo);

        // Draw content on the page
        canvas = page.getCanvas();

        initializePaint();

        float startingY = 0;
        curY = 0;

        textX = marginX + imageWidth + padding;
        textWidth = pageInfo.getPageWidth() - textX - marginX;

        float pageContentHeight = pageInfo.getPageHeight() - 2*margin;

        drawLine("Report", marginX, titlePaint, titleLineHeight);

        for(Item item : items){

            List <String> descriptionLines = generateLines(item.getDescription(), textWidth, paint);

            float itemContainerHeight = calculateContainerHeight(reducedInfoMode, descriptionLines.size());

            if(itemContainerHeight+ curY > pageContentHeight){
                newPage(pdfDocument);
            }

            startingY = curY;

            drawImage(item);

            if(!reducedInfoMode){
                drawLine("Lot " + item.getId(), textX, subtitlePaint, subtitleLineHeight);
                drawLine(item.getName(), textX, subtitlePaint, subtitleLineHeight);
                drawLine(item.getCategory(), textX, subtitlePaint, subtitleLineHeight);
                drawLine(item.getTimePeriod(), textX, subtitlePaint, subtitleLineHeight);
            }

            for(String line : descriptionLines) {
                drawLine(line, textX, paint, lineHeight);
            }
            curY = startingY + itemContainerHeight + padding;
        }
        // Finish the page
        pdfDocument.finishPage(page);

        Date date = new Date();
        // Create the directory and file
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "Report-" +date.getTime()+".pdf");

        try {
            // Write the document content
            pdfDocument.writeTo(new FileOutputStream(file));
            Toast.makeText(context, "PDF saved to " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error saving PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        // Close the document
        pdfDocument.close();
    }

    private List<String> generateLines(String text, float textWidth, Paint paint){
        List <String> lines = new ArrayList<>();
        StringBuilder line = new StringBuilder();
        String[] words = text.split(" ");
        for(String word : words){
            if(paint.measureText(line + " " + word) <= textWidth){
                line.append((line.length() != 0)?" ":"");
                line.append(word);
            }else{
                lines.add(line.toString());
                line = new StringBuilder();
            }
        }
        if(line.length() > 0)lines.add(line.toString());
        return lines;
    }

    private void drawImage(Item item){
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.notloading);
        try {
            InputStream input = new java.net.URL(item.getMedia().getImagePaths().get(0)).openStream();
            bmp =BitmapFactory.decodeStream(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bitmap scaledbmp = Bitmap.createScaledBitmap(bmp, imageWidth, imageHeight, false);
        canvas.drawBitmap(scaledbmp, marginX, curY + imageYMargin, paint);
    }

    private void drawLine(String text, float x, Paint paint, float lineHeight){
        canvas.drawText(text, x,margin + curY,paint);
        curY += lineHeight;
    }

    private Paint createPaint(int size, boolean bold){
        Paint newPaint = new Paint();
        newPaint.setTextSize(size);
        newPaint.setFakeBoldText(bold);
        return newPaint;
    }

    private void initializePaint(){
        paint = createPaint(10, false);
        subtitlePaint = createPaint(14, true);
        titlePaint = createPaint(36, true);
        subtitleLineHeight = subtitlePaint.descent() - subtitlePaint.ascent() + lineSpacing;
        lineHeight = paint.descent() - paint.ascent() + lineSpacing;
        titleLineHeight = (titlePaint.descent()- titlePaint.ascent()) + lineSpacing;
    }

    private float calculateContainerHeight(boolean reducedInfoMode, int numDescriptionLines){
        float itemContainerHeight = 0;
        if(!reducedInfoMode){
            itemContainerHeight += 4 * subtitleLineHeight;
        }
        itemContainerHeight += numDescriptionLines * lineHeight;
        itemContainerHeight = Math.max(itemContainerHeight, imageHeight);
        return itemContainerHeight;
    }

    private void newPage(PdfDocument pdfDocument){
        pdfDocument.finishPage(page);
        pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pdfDocument.getPages().size() + 1).create();
        page = pdfDocument.startPage(pageInfo);
        canvas = page.getCanvas();
        curY = 0;
    }
}
