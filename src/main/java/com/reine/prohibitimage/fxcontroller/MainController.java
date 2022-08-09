package com.reine.prohibitimage.fxcontroller;

import com.reine.prohibitimage.BootApplication;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.image.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class MainController {

    @FXML
    private ImageView imageView;

    @FXML
    void chooseImage(ActionEvent event) {
        Stage stage = new Stage();
        String home = System.getProperty("user.home");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择图片");
        fileChooser.setInitialDirectory(new File(home));
        // 限制选择的文件类型
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("可执行文件", "*.png"),
                new FileChooser.ExtensionFilter("所有类型", "*.jpeg"));
        File file = fileChooser.showOpenDialog(stage);
        Optional.ofNullable(file).ifPresent(f -> {
            try {
                FileInputStream fis = new FileInputStream(f);
                Image image = new Image(fis);
                imageView.setImage(image);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

    }

    @FXML
    void copyImage(ActionEvent event) {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        if (imageView.getImage() == null) {
            return;
        }
        // 复制到剪贴板
        WritableImage snapshot = imageView.snapshot(null, null);
        ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.put(DataFormat.IMAGE, snapshot);
        clipboard.setContent(clipboardContent);
    }

    @FXML
    void generateImage(ActionEvent event) {
        Image image = imageView.getImage();
        if (image == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("错误");
            alert.setContentText("请先选择图片");
            alert.show();
            return;
        }
        double width = image.getWidth();
        double height = image.getHeight();
        WritableImage writableImage = new WritableImage(image.getPixelReader(), (int) width, (int) height);
        PixelWriter pixelWriter = writableImage.getPixelWriter();
        String url = Objects.requireNonNull(BootApplication.class.getResource("/images/prohibit.png")).toExternalForm();
        Image prohibit = new Image(url, width, height, false, true);
        PixelReader pixelReader = prohibit.getPixelReader();
        // 编辑图片
        for (int i = 0; i < (int) width; i++) {
            for (int j = 0; j < (int) height; j++) {
                int x = pixelReader.getArgb(i, j);
                if (x != 0) {
                    pixelWriter.setArgb(i, j, x);
                }
            }
        }
        imageView.setImage(writableImage);
    }

    @FXML
    void saveImage(ActionEvent event) {
        if (imageView.getImage() == null) {
            return;
        }
        Stage stage = new Stage();
        WritableImage snapshot = imageView.snapshot(null, null);
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(snapshot, null);
        String home = System.getProperty("user.home");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("保存图片");
        fileChooser.setInitialFileName("output");
        fileChooser.setInitialDirectory(new File(home));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("图片", "*.png"));
        File file = fileChooser.showSaveDialog(stage);
        Optional.ofNullable(file).ifPresent(f -> {
            try {
                ImageIO.write(bufferedImage, "png", f);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
