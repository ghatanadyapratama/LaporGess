import com.vaadin.flow.component.icon.VaadinIcon;

public class TestIcon {
    public static void main(String[] args) {
        for (VaadinIcon icon : VaadinIcon.values()) {
            if (icon.name().contains("LEAF") || icon.name().contains("TREE") || icon.name().contains("PLANT") || icon.name().contains("ENV")) {
                System.out.println(icon.name());
            }
        }
        System.out.println("Done.");
    }
}
