package NovaJaxe;
import NovaJaxe.backend.utils.CoolUtil;
import NovaJaxe.novahandlers.*;
import NovaJaxe.fnfaffinity.novahandlers.caches.FileCache;
import NovaJaxe.fnfaffinity.novahandlers.caches.FontCache;
import NovaJaxe.fnfaffinity.novahandlers.caches.ImageCache;
import NovaJaxe.fnfaffinity.states.ExampleState;
import static NovaJaxe.fnfaffinity.novahandlers.NovaMath.getDtFinal;
import static NovaJaxe.fnfaffinity.novahandlers.NovaMath.lerp;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import javax.sound.sampled.Clip;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


import java.awt.*;
import java.util.List;


public class Main extends Application {
    public static String windowTitle = "Window Title";
    public static String windowIcon = "windowIcon.png";
    public static boolean enableDebugTraces = true; // use CoolUtil.trace(); for debug.
    public static double fps = 60; // Window framerate.
    public static NovaState startingState = new ExampleState(); // First state.
    public static double volume = 0.5; // 0.5 is default, is also best.
    public static String resourcePath = "src/main/resources/yourPathHere";



    public static Stage globalStage;
    public static Scene globalScene;
    public static Canvas globalCanvas;
    public static GraphicsContext globalContext;
    public static boolean enableGhosting = true;

    public static Vector<FileCache> cachedFiles = new Vector<FileCache>(0);
    public static Vector<FontCache> cachedFonts = new Vector<FontCache>(0);
    public static Vector<ImageCache> cachedImages = new Vector<ImageCache>(0);


    public static Vector<Object> objects = new Vector<Object>(0);
    static Vector<Object> objectsGlobal = new Vector<Object>(0);
    public static double globalAlpha = 1.0;
    static Boolean borderless = false;
    public static NovaCamera camGame = new NovaCamera(0, 0);

    public static double globalSpriteOffsetX;
    public static double globalSpriteOffsetY;

    @Override
    public void start(Stage stage) throws IOException, NoSuchFieldException, IllegalAccessException {
        globalStage = stage;
        globalStage.setTitle(windowTitle);
        globalStage.initStyle(StageStyle.DECORATED);

        final double W = globalStage.getWidth();
        final double H = globalStage.getHeight();
        globalCanvas = new Canvas(1280, 735);
        globalContext = globalCanvas.getGraphicsContext2D();
        globalStage.getIcons().add(new Image(Objects.requireNonNull(Main.class.getResourceAsStream(windowIcon))));
        Dimension size
                = Toolkit.getDefaultToolkit().getScreenSize();

        // width will store the width of the screen
        int width = (int)size.getWidth();

        // height will store the height of the screen
        int height = (int)size.getHeight();


        if (NovaWindow.width == 0)
        {
            NovaWindow.width = 1280;
            NovaWindow.height = 720;
        }

        globalStage.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (borderless)
                    globalStage.setAlwaysOnTop(observable.getValue());

            }
        });
        globalStage.setScene(new Scene(new Group(globalCanvas)));
        //music.setVolume(volume);
        globalStage.show();

        // Switch to starting state.
        NovaState.switchState(startingState);
        
        globalStage.getScene().setOnKeyPressed(e -> {
            for (String i : NovaKeys.keyList) {
                try {
                    NovaKey key = (NovaKey) NovaKeys.class.getDeclaredField(i).get(null);
                    KeyCode code = (KeyCode) KeyCode.class.getDeclaredField(i).get(null);
                    if (e.getCode() == code) {
                        if (!key.pressed && key.frame == -1) {
                            key.pressed = true;
                            key.justPressed = true;
                            key.frame = 1;
                            // Debug Code
                            //System.out.println("Pressed: " + i);
                        }
                    }
                } catch (IllegalAccessException ex) {
                    throw new RuntimeException(ex);
                } catch (NoSuchFieldException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        globalStage.getScene().setOnKeyReleased(e -> {
            for (String i : NovaKeys.keyList) {
                try {
                    NovaKey key = (NovaKey) NovaKeys.class.getDeclaredField(i).get(null);
                    KeyCode code = (KeyCode) KeyCode.class.getDeclaredField(i).get(null);
                    if (e.getCode() == code) {
                        if (key.pressed && key.frame == -1) {
                            key.pressed = false;
                            key.justReleased = true;
                            key.frame = 1;
                            // Debug Code
                            //System.out.println("Released: " + i);
                        }
                    }
                } catch (IllegalAccessException ex) {
                    throw new RuntimeException(ex);
                } catch (NoSuchFieldException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }




    public static void add(NovaText text) {
        objects.add(text);
    }

    public static void add(NovaGraphic graphic) {
        objects.add(graphic);
    }

    public static void add(CharacterIcon text) {
        objects.add(text);
    }

    public static void add(NovaSprite sprite) {
        objects.add(sprite);
        return;
    }
    public static void add(NovaGroup group) {
        objects.add(group);
        return;
    }
    public static void add(NovaSpriteGroup group) {
        objects.add(group);
        return;
    }
    public static void add(NovaAnimSprite sprite) {
        objects.add(sprite);
        return;
    }
    public static void add(FunkinCharacter sprite) {
        objects.add(sprite);
        return;
    }
    public static void add(NovaAlphabet sprite) {
        objects.add(sprite);
        return;
    }
    public static void addGlobal(NovaSprite sprite) {
        objectsGlobal.add(sprite);
        return;
    }
    public static void addGlobal(NovaAnimSprite sprite) {
        objectsGlobal.add(sprite);
        return;
    }
    public static String pathify(String str) {
        return resourcePath + "/" + str;
    }
    private static String lastError;
    private static int errcount = 0;
    private static boolean updateVolume = true;
    public static void global_update() throws ParserConfigurationException, IOException, SAXException {
        /* Uncomment this code block if you want to enable the options file.
        try {
            options = CoolUtil.parseJson("data/options");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        NovaKey fullScreenKey = null;
        String fullScreenKeyName = "";
        for (Object obj : options.getJSONArray("sections")) {
            JSONObject daObj = (JSONObject) obj;
            if (Objects.equals(daObj.getString("title"), "Controls")) {
                try {
                    fullScreenKey = (NovaKey) NovaKeys.class.getDeclaredField(daObj.getJSONObject("options").getString("fullscreen")).get(null);
                    fullScreenKeyName = daObj.getJSONObject("options").getString("fullscreen");
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        if (fullScreenKey != null)
            if (fullScreenKey.justPressed) {
                globalStage.setFullScreenExitHint("Press " + fullScreenKeyName + " to exit fullscreen.");
                globalStage.setFullScreen(!globalStage.isFullScreen());
            }


        if (NovaKeys.EQUALS.justPressed || NovaKeys.ADD.justPressed) {
            volume += 0.1;
            if (volume > 1)
                volume = 1;
            CoolUtil.trace(volume);
            CoolUtil.setOption("volume", volume);
            updateVolume = true;
        }
        if (NovaKeys.MINUS.justPressed || NovaKeys.SUBTRACT.justPressed) {
            volume -= 0.1;
            if (volume < 0)
                volume = 0;
            CoolUtil.trace(volume);
            CoolUtil.setOption("volume", volume);
            updateVolume = true;
        }
        */ // Comment Ends Here
        
        
        double canvasScaleX = globalStage.getWidth()/1280;
        double canvasScaleY = globalStage.getHeight()/720;
        double finalScale = Math.max(canvasScaleX, canvasScaleY);
        globalCanvas.setTranslateX((globalStage.getWidth()/2)-(1280/2));
        globalCanvas.setTranslateY(((globalStage.getHeight()-30)/2)-(720/2));
        globalCanvas.setScaleX(finalScale*camGame.zoom);
        globalCanvas.setScaleY(finalScale*camGame.zoom);
        globalContext.setFill(Paint.valueOf("0x00000000"));
        globalContext.fillRect(0-(10000000/2), 0-(10000000/2), globalCanvas.getWidth()+10000000, globalCanvas.getHeight()+10000000);
     
        Vector<Object> spritesDrawn = new Vector<Object>(0);
        for (Object object : objects) {
            if (!spritesDrawn.contains(object)) {
                spritesDrawn.add(object);
                if (object.getClass() == NovaText.class) {
                    if (((NovaText) object).alive && ((NovaText) object).camera.visible) {
                        drawText((NovaText) object);
                    }
                }
                if (object.getClass() == NovaSprite.class || object.getClass() == StageSprite.class) {
                    if (((NovaSprite) object).alive && ((NovaSprite) object).camera.visible) {
                        drawSprite((NovaSprite) object);
                    }
                }
                if (object.getClass() == Note.class || object.getClass() == SustainNote.class || object.getClass() == Strum.class || object.getClass() == StageAnimSprite.class) {
                    if (((NovaAnimSprite) object).alive && ((NovaAnimSprite) object).camera.visible) {
                        drawSprite((NovaAnimSprite) object);
                    }
                }
                if (object.getClass() == NovaAnimSprite.class || object.getClass() == FunkinCharacter.class) {
                    if (((NovaAnimSprite) object).alive && ((NovaAnimSprite) object).camera.visible) {
                        drawSprite((NovaAnimSprite) object);
                    }
                }
                if (object.getClass() == NovaAlphabet.class && ((NovaAlphabet) object).camera.visible) {
                    drawSprite((NovaAlphabet) object);
                }
                if (object.getClass() == CharacterIcon.class) {
                    if (((CharacterIcon) object).alive && ((CharacterIcon) object).visible && ((CharacterIcon) object).camera.visible)
                        drawSprite((CharacterIcon) object);
                }
                if (object.getClass() == NovaGraphic.class && ((NovaGraphic) object).camera.visible) {
                    NovaGraphic daObject = (NovaGraphic) object;

                    globalContext.save();
                    globalContext.setGlobalAlpha(daObject.alpha);
                    globalContext.setFill(Paint.valueOf(daObject.color));
                    globalContext.fillRect(
                            daObject.x + (daObject.camera.x * daObject.scrollX) + globalSpriteOffsetX,
                            daObject.y + (daObject.camera.y * daObject.scrollY) + globalSpriteOffsetY,
                            daObject.width,
                            daObject.height
                    );
                    globalContext.setGlobalAlpha(1);
                    globalContext.restore();
                }
                if (object.getClass() == NovaGroup.class || object.getClass() == StrumLine.class) {
                    if (object.getClass() == StrumLine.class) {
                        if (((StrumLine) object).visible) {
                            for (NovaAnimSprite member : ((NovaGroup) object).members) {
                                //System.out.println(member);
                                if (member.getClass() == Strum.class && member.camera.visible) {
                                    if (member.alive) {
                                        drawSprite(member);
                                    }
                                }
                            }
                        }
                    }
                }
                if (object.getClass() == NovaSpriteGroup.class) {
                    for (NovaSprite member : ((NovaSpriteGroup) object).members) {
                        if (member.alive && member.camera.visible) {
                            drawSprite(member);
                        }
                    }
                }
                if (object.getClass() == NovaSprite.class) {
                    if (((NovaSprite) object).alive && ((NovaSprite) object).camera.visible) {
                        drawSprite((NovaSprite) object);
                    }
                }
            }
        }

        for (Object object : objectsGlobal) {
            if (object.getClass() == NovaSprite.class) {
                if (((NovaSprite) object).alive) {
                    drawSprite((NovaSprite) object);
                }
            }
            if (object.getClass() == NovaAnimSprite.class || object.getClass() == FunkinCharacter.class) {
                if (((NovaAnimSprite) object).alive) {
                    drawSprite((NovaAnimSprite) object);
                }
            }
            if (object.getClass() == NovaAlphabet.class) {
                drawSprite((NovaAlphabet) object);
            }
            if (object.getClass() == NovaSprite.class) {
                if (((NovaSprite) object).alive) {
                    drawSprite((NovaSprite) object);
                }
            }
        }


        for (String i : NovaKeys.keyList) {
            try {
                NovaKey key = (NovaKey) NovaKeys.class.getDeclaredField(i).get(null);
                key.update();
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            } catch (NoSuchFieldException ex) {
                throw new RuntimeException(ex);
            }
        }



        for (Object obj : options.getJSONArray("sections")) {
            JSONObject daObj = (JSONObject) obj;
            if (Objects.equals(daObj.getString("title"), "Global")) {
                if (updateVolume) {
                    updateVolume = false;
                    volume = daObj.getJSONObject("options").getDouble("volume");
                    if (volume > 1) {
                        CoolUtil.setOption("volume", 1);
                        volume = 1;
                    } else if (volume < 0.01) {
                        CoolUtil.setOption("volume", 0);
                        volume = 0;
                    }
                }

            }
        }
        CoolUtil.setVolume(volume);



        //NovaKeys.update();
    }
    public static void drawSprite(NovaSprite object) {
        if (object.visible) {
            final double daAlpha = (object.alpha * globalAlpha);
            final double daAngle = Math.toRadians(object.angle);
            globalContext.setGlobalAlpha(daAlpha);
            globalContext.drawImage(object.img, 0, 0, object.img.getWidth(), object.img.getHeight(), object.x + (object.camera.x * object.scrollX) + globalSpriteOffsetX, object.y + (object.camera.y * object.scrollY) + globalSpriteOffsetY, object.img.getWidth() * object.scaleX, object.img.getHeight() * object.scaleY);
            globalContext.setGlobalAlpha(1);
        }
        return;
    }
    public static void drawSprite(CharacterIcon objectFull) {
        int xMult = 1;
        double xOffset = 0;
        if (objectFull.flipX) {
            xMult = -1;
            xOffset = (objectFull.img.getWidth()/objectFull.segments)*objectFull.scaleX;
        }
        final double daAlpha = (objectFull.alpha * globalAlpha);
        globalContext.setGlobalAlpha(daAlpha);
        globalContext.drawImage(objectFull.img, (objectFull.img.getWidth() / objectFull.segments) * objectFull.frame, 0, (objectFull.img.getWidth() / objectFull.segments), objectFull.img.getHeight(), objectFull.camera.x + objectFull.x + globalSpriteOffsetX + xOffset, objectFull.y + (objectFull.camera.y * objectFull.scrollY) + globalSpriteOffsetY, ((objectFull.img.getWidth() / objectFull.segments) * objectFull.scaleX) * xMult, objectFull.img.getHeight() * objectFull.scaleY);
        globalContext.setGlobalAlpha(1);
    }
    public static void drawSprite(NovaAlphabet objectFull) {
        if (objectFull.icon != null)
            globalContext.drawImage(objectFull.icon.img, 0, 0, objectFull.icon.img.getWidth()/2, objectFull.icon.img.getHeight(), objectFull.width + objectFull.camera.x + objectFull.x + globalSpriteOffsetX, (-20 + (objectFull.camera.y) + objectFull.y*2) + globalSpriteOffsetY, (objectFull.icon.img.getWidth()/2) * objectFull.icon.scaleX, objectFull.icon.img.getHeight() * objectFull.icon.scaleY);
        double fullWidth = 0;
        for (NovaSprite object : objectFull.sprites) {
            if (object != null && object.visible) {
                final double daAlpha = (object.alpha * globalAlpha);
                globalContext.setGlobalAlpha(daAlpha);
                globalContext.drawImage(object.img, 0, 0, object.img.getWidth(), object.img.getHeight(), object.x + (object.camera.x * object.scrollX) + objectFull.x + globalSpriteOffsetX, object.y + (object.camera.y * object.scrollY) + objectFull.y + globalSpriteOffsetY, object.img.getWidth() * object.scaleX, object.img.getHeight() * object.scaleY);
                globalContext.setGlobalAlpha(1);
                fullWidth += object.img.getWidth()*object.scaleX;
            }
        }
        objectFull.fullWidth = fullWidth;
        return;
    }
    public static void drawSprite(NovaAnimSprite object) {
        if (!object.visible) return;
        //final Image tempImg = new Image(Objects.requireNonNull(Main.class.getResourceAsStream("images/" + object.path + ".png")));
        try {
            final String filepath = pathify("images/" + object.path + ".xml");

            /*
            // In util
            Map<String, Document> cachedPaths = new HashMap<>();
            cachedPaths.put(pathNameWhatever, xmlDoc);

            // In main
            if (cachedPaths.containsKey(pathNameWhatever)) {

            }
            */
            Document document = null;
            File file = null;
            boolean fileCached = false;
            for (FileCache fileCache : cachedFiles) {
                if (Objects.equals(fileCache.path, filepath)) {
                    file = fileCache.fileData;
                    document = fileCache.document;
                    fileCached = true;
                }
            }

            if (!fileCached) {
                //System.out.println("File not cached! Caching.");
                file = new File(filepath);
                final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                final DocumentBuilder db = dbf.newDocumentBuilder();
                document = db.parse(file);
                cachedFiles.add(new FileCache(filepath, file, document));
            } else {
                //System.out.println("File is cached!");
            }
            document.getDocumentElement().normalize();
            //System.out.println("Root element :" + document.getDocumentElement().getNodeName());
            final NodeList nList = document.getElementsByTagName("SubTexture");
            int FramRef = 0;
            int animLength = 0;
            String prefix = "";
            for (NovaAnimController controller : object.animations) {
                if (Objects.equals(controller.name, object.curAnim)) {
                    prefix = controller.prefix;
                    //System.out.println(controller.prefix);
                }
            }
            final String curAnim = object.curAnim;
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String frameName = eElement.getAttribute("name");
                    if (frameName.startsWith(prefix)) {
                        animLength += 1;
                    }
                }
            }
            for (int i = 0; i < object.animations.size(); i++) {
                if (object.animations.get(i).curFrame > animLength - 1) {
                    if (object.animations.get(i).loop) {
                        object.animations.set(i, object.animations.get(i)).curFrame = 0;
                        object.curAnim = curAnim;
                    } else {
                        object.animations.set(i, object.animations.get(i)).curFrame = animLength - 1;
                    }
                }
                if (object.animations.get(i).loop) {
                    object.animations.set(i, object.animations.get(i)).curFrame += object.animations.get(i).fps / fps;
                } else {
                    if (object.animations.get(i).curFrame != animLength - 1) {
                        object.animations.set(i, object.animations.get(i)).curFrame += object.animations.get(i).fps / fps;
                    }
                }
                FramRef = (int) Math.round(object.animations.get(i).curFrame);
            }
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                Node prevFrame = null;
                try {
                    prevFrame = nList.item(temp - 1);
                } catch (Exception ignore) {
                }

                //System.out.println("\nCurrent Element: " + nNode.getNodeName());
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    final Element eElement = (Element) nNode;
                    final Element eElementPrev = (Element) prevFrame;
                    final String frameName = eElement.getAttribute("name");
                    //System.out.println(FramRef);
                    int frameFix = (int) Math.round(Double.parseDouble(Double.toString(FramRef).substring(0, 2)));
                    

                    //System.out.println(frameName);
                    if (frameName.startsWith(prefix) && Math.round(Double.parseDouble(frameName.substring(frameName.length() - 4))) == frameFix) {
                        double frameX = 0;
                        double frameY = 0;
                        double frameW = 0;
                        double frameH = 0;
                        double X = 0;
                        double Y = 0;
                        double W = 0;
                        double H = 0;
                        try {
                            frameX = Integer.parseInt(eElement.getAttribute("frameX"));
                            frameY = Integer.parseInt(eElement.getAttribute("frameY"));
                            frameW = Integer.parseInt(eElement.getAttribute("frameWidth"));
                            frameH = Integer.parseInt(eElement.getAttribute("frameHeight"));
                            X = Integer.parseInt(eElement.getAttribute("x"));
                            Y = Integer.parseInt(eElement.getAttribute("y"));
                            W = Integer.parseInt(eElement.getAttribute("width"));
                            H = Integer.parseInt(eElement.getAttribute("height"));
                        } catch (Exception e) {
                            try {
                                //assert eElementPrev != null;
                                frameX = Integer.parseInt(eElementPrev.getAttribute("frameX"));
                                frameY = Integer.parseInt(eElementPrev.getAttribute("frameY"));
                                frameW = Integer.parseInt(eElementPrev.getAttribute("frameWidth"));
                                frameH = Integer.parseInt(eElementPrev.getAttribute("frameHeight"));
                                X = Integer.parseInt(eElementPrev.getAttribute("x"));
                                Y = Integer.parseInt(eElementPrev.getAttribute("y"));
                                W = Integer.parseInt(eElementPrev.getAttribute("width"));
                                H = Integer.parseInt(eElementPrev.getAttribute("height"));
                            } catch (Exception ignore) {
                                X = Integer.parseInt(eElement.getAttribute("x"));
                                Y = Integer.parseInt(eElement.getAttribute("y"));
                                W = Integer.parseInt(eElement.getAttribute("width"));
                                H = Integer.parseInt(eElement.getAttribute("height"));
                                frameX = 0;
                                frameY = 0;
                                frameW = H;
                                frameH = W;
                            }
                        }
                        if (object.visible) {
                            final double daAlpha = (object.alpha * globalAlpha);
                            globalContext.setGlobalAlpha(daAlpha);
                            double offsetX = 0;
                            double offsetY = 0;
                            for (NovaAnimController anim : object.animations) {
                                if (Objects.equals(anim.name, object.curAnim)) {
                                    offsetX = anim.offsetX;
                                    offsetY = anim.offsetY;
                                }
                            }
                            int flipInt = 0;
                            if (object.flipX) {
                                flipInt = -1;
                            } else {
                                flipInt = 1;
                            }
                            object.frameWidth = frameW;
                            object.frameHeight = frameH;
                            //globalContext.rotate(Math.toRadians(object.angle));
                            globalContext.drawImage(object.img, X, Y, W, H, (object.x - (frameX * (object.scaleX * flipInt)) + (object.camera.x * object.scrollX)) + (offsetX*flipInt) + globalSpriteOffsetX, (object.y - (frameY * object.scaleX) + (object.camera.y * object.scrollY)) + offsetY + globalSpriteOffsetY, (W * (object.scaleX * flipInt)), H * object.scaleY);
                            globalContext.setGlobalAlpha(1);
                        }
                        //break;
                    }
                }
            }
        } catch (Exception ignore) {}
        return;
    }

    public static Font getFont(String path, int size) {
        for (FontCache font : cachedFonts) {
            if (font.path == path) {
                return font.font;
            }
        }
        InputStream stream = Main.class.getResourceAsStream("fonts/" + path);
        Font daFont = Font.loadFont(stream, size);
        cachedFonts.add(new FontCache(daFont, path));
        return daFont;
    }

    static DropShadow ds = new DropShadow();
    public static void drawText(NovaText textObj) {
        if (!textObj.visible) return;
        //final double daAlpha = (textObj.alpha * globalAlpha);
        globalContext.setGlobalAlpha(textObj.alpha);
        globalContext.setFill(Paint.valueOf(textObj.color));
        globalContext.setFont(getFont(textObj.path, textObj.size));
        globalContext.setTextAlign(textObj.alignment);
        //globalContext.set
        ds.setBlurType(BlurType.ONE_PASS_BOX);
        ds.setColor(Color.web(textObj.borderColor.replace("#", "0x"), textObj.borderAlpha));
        ds.setRadius(textObj.borderSize);
        globalContext.setEffect(ds);
        globalContext.fillText(textObj.text, textObj.x + (textObj.camera.x * textObj.scrollX), textObj.y + (textObj.camera.y * textObj.scrollY));
        globalContext.setGlobalAlpha(1);
        globalContext.setEffect(null);
    }

    public static void clearObj() {
        objects = new Vector<Object>(0);
    }

    public static String readFileAsString(String file)throws Exception
    {
        return new String(Files.readAllBytes(Paths.get(file)));
    }
    public static void main(String[] args) {
        launch();
    }

    public static void addScriptSprite(Object object) {
        CoolUtil.trace(object);
        objects.add((NovaSprite) object);
    }
}


