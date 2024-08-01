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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class PDFCreator {
    private static final int PAGE_WIDTH = 595;
    private static final int PAGE_HEIGHT = 842;
    private static final int IMAGE_HEIGHT = 100;
    private static final int IMAGE_WIDTH = 100;
    private static final float MARGIN_X = 80;
    private static final float PADDING = 20;
    private static final float LINE_SPACING = 5;
    private static final int IMAGE_Y_MARGIN = 40;

    private Context context;
    private PdfDocument pdfDocument;
    private PdfDocument.PageInfo pageInfo;
    private PdfDocument.Page page;
    private Canvas canvas;
    private Paint paint;
    private Paint subtitlePaint;
    private Paint titlePaint;

    private float curY;
    private float textX;
    private float textWidth;
    private float lineHeight;
    private float subtitleLineHeight;
    private float titleLineHeight;

    public void createPdf(Context context, List<Item> items, boolean reducedInfoMode) {
        this.context = context;
        pdfDocument = new PdfDocument();

        pageInfo = new PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, 1).create();
        page = pdfDocument.startPage(pageInfo);
        canvas = page.getCanvas();

        initializePaint();

        curY = 0;
        textX = MARGIN_X + IMAGE_WIDTH + PADDING;
        textWidth = pageInfo.getPageWidth() - textX - MARGIN_X;

        float pageContentHeight = pageInfo.getPageHeight() - 2 * MARGIN_X;
        drawLine("Report", MARGIN_X, titlePaint, titleLineHeight);

        for (Item item : items) {
            List<String> descriptionLines = generateLines(item.getDescription(), textWidth, paint);
            float itemContainerHeight = calculateContainerHeight(reducedInfoMode, descriptionLines.size());

            if (itemContainerHeight + curY > pageContentHeight) {
                newPage();
            }

            drawImage(item);

            if (!reducedInfoMode) {
                drawLine("Lot " + item.getId(), textX, subtitlePaint, subtitleLineHeight);
                drawLine(item.getName(), textX, subtitlePaint, subtitleLineHeight);
                drawLine(item.getCategory(), textX, subtitlePaint, subtitleLineHeight);
                drawLine(item.getTimePeriod(), textX, subtitlePaint, subtitleLineHeight);
            }

            for (String line : descriptionLines) {
                drawLine(line, textX, paint, lineHeight);
            }
            curY += itemContainerHeight + PADDING;
        }

        pdfDocument.finishPage(page);
        savePdf();
    }

    private void savePdf() {
        Date date = new Date();
        File file = new File(context.getExternalFilesDir(null), "Report-" + date.getTime() + ".pdf");

        try (FileOutputStream fos = new FileOutputStream(file)) {
            pdfDocument.writeTo(fos);
            Toast.makeText(context, "PDF saved to " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error saving PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        pdfDocument.close();
    }

    private List<String> generateLines(String text, float textWidth, Paint paint) {
        List<String> lines = new ArrayList<>();
        StringBuilder line = new StringBuilder();
        String[] words = text.split(" ");

        for (String word : words) {
            if (paint.measureText(line + " " + word) <= textWidth) {
                line.append((line.length() != 0) ? " " : "").append(word);
            } else {
                lines.add(line.toString());
                line.setLength(0);
                line.append(word);
            }
        }

        if (line.length() > 0) lines.add(line.toString());
        return lines;
    }

    private void drawImage(Item item) {
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.notloading);
        try {
            URL url = new URL(item.getMedia().getImagePaths().get(0));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            bmp = BitmapFactory.decodeStream(input);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Bitmap scaledBmp = Bitmap.createScaledBitmap(bmp, IMAGE_WIDTH, IMAGE_HEIGHT, false);
        canvas.drawBitmap(scaledBmp, MARGIN_X, curY + IMAGE_Y_MARGIN, paint);
    }

    private void drawLine(String text, float x, Paint paint, float lineHeight) {
        canvas.drawText(text, x, MARGIN_X + curY, paint);
        curY += lineHeight;
    }

    private void initializePaint() {
        paint = createPaint(10, false);
        subtitlePaint = createPaint(14, true);
        titlePaint = createPaint(36, true);
        subtitleLineHeight = subtitlePaint.descent() - subtitlePaint.ascent() + LINE_SPACING;
        lineHeight = paint.descent() - paint.ascent() + LINE_SPACING;
        titleLineHeight = (titlePaint.descent() - titlePaint.ascent()) + LINE_SPACING;
    }

    private Paint createPaint(int size, boolean bold) {
        Paint newPaint = new Paint();
        newPaint.setTextSize(size);
        newPaint.setFakeBoldText(bold);
        return newPaint;
    }

    private float calculateContainerHeight(boolean reducedInfoMode, int numDescriptionLines) {
        float itemContainerHeight = 0;
        if (!reducedInfoMode) {
            itemContainerHeight += 4 * subtitleLineHeight;
        }
        itemContainerHeight += numDescriptionLines * lineHeight;
        itemContainerHeight = Math.max(itemContainerHeight, IMAGE_HEIGHT);
        return itemContainerHeight;
    }

    private void newPage() {
        pdfDocument.finishPage(page);
        pageInfo = new PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, pdfDocument.getPages().size() + 1).create();
        page = pdfDocument.startPage(pageInfo);
        canvas = page.getCanvas();
        curY = 0;
    }
}
