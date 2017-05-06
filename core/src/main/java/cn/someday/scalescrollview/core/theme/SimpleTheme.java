package cn.someday.scalescrollview.core.theme;

import android.graphics.Color;

/**
 * 默认样式
 * @author shaojunjie on 17-5-6
 * @Email fgnna@qq.com
 *
 */
public class SimpleTheme extends BaseTheme
{
    public SimpleTheme()
    {
        setLargeLineWidth(3);
        setLargeLineLength(20);
        setLargeLineColor(Color.GRAY);
        setSmallLineWidth(1);
        setSmallLineLength(5);
        setSmallLineColor(Color.GRAY);
        setTextSize(10);
        setTextColor(Color.GRAY);
        setSpace(20);
    }
}
