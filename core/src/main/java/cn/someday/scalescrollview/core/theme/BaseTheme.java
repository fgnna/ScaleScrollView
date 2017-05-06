package cn.someday.scalescrollview.core.theme;

/**
 * 刻度的样式控制类
 * 用户自定义样式必须继承此类
 * 建义在构造方法中设置样式值，参考{@link SimpleTheme}
 *
 * @author shaojunjie on 17-5-6
 * @Email fgnna@qq.com
 */
public abstract class BaseTheme
{
    /**
     * 大等份线条粗度
     */
    private int largeLineWidth;
    /**
     * 大等份线条长度
     */
    private int largeLineLength;
    /**
     * 大等份线条颜色
     */
    private int largeLineColor;

    /**
     * 小等份线条粗度
     */
    private int smallLineWidth;
    /**
     * 小等份线条长度
     */
    private int smallLineLength;
    /**
     * 小等份线条颜色
     */
    private int smallLineColor;
    /**
     * 大等份数值字体大小
     */
    private int textSize;
    /**
     * 每小份的间距 px
     */
    private int space;

    /**
     * 大等份数值字体颜色
     */
    private int textColor;

    public void setLargeLineWidth(int largeLineWidth) {
        this.largeLineWidth = largeLineWidth;
    }

    public void setLargeLineLength(int largeLineLength) {
        this.largeLineLength = largeLineLength;
    }

    public void setLargeLineColor(int largeLineColor) {
        this.largeLineColor = largeLineColor;
    }

    public void setSmallLineWidth(int smallLineWidth) {
        this.smallLineWidth = smallLineWidth;
    }

    public void setSmallLineLength(int smallLineLength) {
        this.smallLineLength = smallLineLength;
    }

    public void setSmallLineColor(int smallLineColor) {
        this.smallLineColor = smallLineColor;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getSpace() {
        return space;
    }

    public void setSpace(int space) {
        this.space = space;
    }

    public int getLargeLineWidth() {
        return largeLineWidth;
    }

    public int getLargeLineLength() {
        return largeLineLength;
    }

    public int getLargeLineColor() {
        return largeLineColor;
    }

    public int getSmallLineWidth() {
        return smallLineWidth;
    }

    public int getSmallLineLength() {
        return smallLineLength;
    }

    public int getSmallLineColor() {
        return smallLineColor;
    }

    public int getTextSize() {
        return textSize;
    }

    public int getTextColor() {
        return textColor;
    }
}
