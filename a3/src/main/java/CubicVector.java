public class CubicVector {
    public int index;
    public float startX;
    public float startY;
    public float endX;
    public float endY;
    public float controlX1;
    public float controlY1;
    public float controlX2;
    public float controlY2;
    public String color;
    public int lineType;
    public float lineThickness;

    CubicVector(int index,
                float startX,
                float startY,
                float endX,
                float endY,
                float controlX1,
                float controlY1,
                float controlX2,
                float controlY2,
                String color,
                int lineType,
                float lineThickness) {
        this.index = index;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.controlX1 = controlX1;
        this.controlY1 = controlY1;
        this.controlX2 = controlX2;
        this.controlY2 = controlY2;
        this.color = color;
        this.lineType = lineType;
        this.lineThickness = lineThickness;

    }

}
