package com.miguelgarcia.android.weatherforecast.object;


/**
 * class whit navigation drawer information.
 */
public class NavegationDrawerItem  {

    private String tittle;
    private int icon;

    // Constructor.
    public NavegationDrawerItem (String tittle, int icon) {
        this.tittle = tittle;
        this.icon = icon;
    }

    // Getters.
    public String getTittle() { return tittle; }

    public int getIcon() { return icon; }

    // Setters.
    public void setTittle(String tittle) { this.tittle = tittle; }

    public void setIcon(int icon) { this.icon = icon; }
}
