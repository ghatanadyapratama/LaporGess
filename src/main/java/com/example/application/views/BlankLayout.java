package com.example.application.views;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.RouterLayout;

public class BlankLayout extends Div implements RouterLayout {
    public BlankLayout() {
        setSizeFull();
        getStyle().set("margin", "0");
        getStyle().set("padding", "0");
    }
}
