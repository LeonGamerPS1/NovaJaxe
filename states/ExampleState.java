package com.example.fnfaffinity.states;

import com.example.fnfaffinity.backend.utils.ConsoleColors;
import com.example.fnfaffinity.backend.utils.CoolUtil;
import com.example.fnfaffinity.backend.utils.MusicBeatState;
import com.example.fnfaffinity.backend.utils.WindowUtil;
import com.example.fnfaffinity.novahandlers.*;

public class ExampleState extends MusicBeatState {

    public void preUpdate() {
        super.preUpdate();
    }

    public void update() {
        super.update();
    }

    public void postUpdate() {
        super.postUpdate();
    }

    public void beat() {
        super.beat();
    }

    public void step() {
        super.step();
    }

    public void create() {
        super.create();

        NovaSprite exampleSprite = new NovaSprite("imagePath in images/", x, y);
        add(exampleSprite);
    }

    public void postCreate() {
        super.postCreate();
    }

    public void destroy() {
        super.destroy();
    }
}
