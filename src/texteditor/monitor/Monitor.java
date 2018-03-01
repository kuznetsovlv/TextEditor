package texteditor.monitor;

public interface Monitor {
    public boolean isAvailableFor(Runnable r);
    public void setOccupied(Runnable r);
    public void setFree();
}
