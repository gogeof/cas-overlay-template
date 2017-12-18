package sglca.helper.utils.common;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import org.szwj.ca.identityauthsrv.util.common.FileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class GenPDFUtil {

    public static void writeDataToPDF(String fileName, String fileContent)
        throws IOException, DocumentException {

        FileOutputStream out = null;
        try {
            File file = new File(fileName);
            out = FileUtil.openOutputStream(file, false);
        } finally {
            if (out != null) {
                out.close();
            }
        }

        Document document = new Document();
        PdfWriter writer = PdfWriter
            .getInstance(document, new FileOutputStream(fileName));
        document.open();

        String userDir = System.getProperty("user.dir");
        BaseFont baseFont = BaseFont
            .createFont(userDir + "/config/SIMYOU.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        Font font = new Font(baseFont);

        String decodeContent = decodeUnicode(fileContent.toCharArray(), 0, fileContent.length());
        document.add(new Paragraph(decodeContent, font));
        document.close();
        writer.close();
    }

    /**
     * 从 Unicode 形式的字符串转换成对应的编码的特殊字符串。 如 "\u9EC4" to "黄".
     * Converts encoded \\uxxxx to unicode chars
     * and changes special saved chars to their original forms
     *
     * @param in Unicode编码的字符数组。
     * @param off 转换的起始偏移量。
     * @param len 转换的字符长度。
     * @return 完成转换，返回编码前的特殊字符串。
     */
    private static String decodeUnicode(char[] in, int off, int len) {
        char aChar;
        char[] out = new char[len]; // 只短不长
        int outLen = 0;
        int end = off + len;

        while (off < end) {
            aChar = in[off++];
            if (aChar == '\\') {
                aChar = in[off++];
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = in[off++];
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException("Malformed \\uxxxx encoding.");
                        }
                    }
                    out[outLen++] = (char) value;
                } else {
                    if (aChar == 't') {
                        aChar = '\t';
                    } else if (aChar == 'r') {
                        aChar = '\r';
                    } else if (aChar == 'n') {
                        aChar = '\n';
                    } else if (aChar == 'f') {
                        aChar = '\f';
                    }
                    out[outLen++] = aChar;
                }
            } else {
                out[outLen++] = (char) aChar;
            }
        }
        return new String(out, 0, outLen);
    }
}
