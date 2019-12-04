package com.kairlec.pojo;

import lombok.Data;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

@Data
public class Captcha {
    private String captchaString;
    private BufferedImage bufferedImage;

    @Override
    public String toString() {
        return captchaString;
    }

    public void outputStream(OutputStream outputStream) throws IOException {
        ImageIO.write(bufferedImage, "jpg", outputStream);
    }

    public boolean check(String captchaString) {
        return captchaString.equalsIgnoreCase(this.captchaString);
    }

}
