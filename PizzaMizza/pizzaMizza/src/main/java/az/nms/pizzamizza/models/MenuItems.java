package az.nms.pizzamizza.models;

public class MenuItems {

    public int icon;
    public int title;

    public MenuItems(int icon, int title) {
        // TODO Auto-generated constructor stub
        this.icon = icon;
        this.title = title;

    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

}
