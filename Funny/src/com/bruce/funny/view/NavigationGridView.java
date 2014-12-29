package com.bruce.funny.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;

public class NavigationGridView extends GridView {
    private Context context;
    
    public NavigationGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context= context;
    }
    
    public NavigationGridView(Context context) {
        super(context);
    }
    
    public NavigationGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
    // 该自定义控件只是重写了GridView的onMeasure方法，使其不会出现滚动条，ScrollView嵌套ListView也是同样的道理，不再赘述。
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
    
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        View localView1 = getChildAt(0);
        int column = getWidth() / localView1.getWidth();//列
        int childCount = getChildCount();//gridview显示子项数量
        Paint localPaint;
        localPaint = new Paint();
        localPaint.setStyle(Paint.Style.STROKE);
        localPaint.setColor(getContext().getResources().getColor(android.R.color.darker_gray));
        for (int i = 0; i < childCount; i++) {
            View cellView = getChildAt(i);

            if(i / column == 0){//第一行顶部横线
                canvas.drawLine(cellView.getLeft(), cellView.getTop(), cellView.getRight(), cellView.getTop(), localPaint);
            }
            if(i / column == (childCount-1) / column){//最后一行底部横线
                canvas.drawLine(cellView.getLeft(), cellView.getBottom() - 1, cellView.getRight(), cellView.getBottom() - 1, localPaint);
            }

            if ((i + 1) % column == 0) {//画每一行的最后一列的item，底部的横线
                if(childCount % column == 0 && (i+1) > ((childCount / column -1)*column)) continue;
                canvas.drawLine(cellView.getLeft(), cellView.getBottom(), cellView.getRight()+10, cellView.getBottom(),
                        localPaint);
            } else if ((i + 1) > (childCount - (childCount % column))) {//画非第一行的非最后一列item：竖线
                canvas.drawLine(cellView.getRight(), cellView.getTop()-10, cellView.getRight(), cellView.getBottom()+12,
                        localPaint);
            } else {//画第一行的非最后一列,横竖线
                int bottom = 0;
                if(childCount % column == 0 && (i+1) > ((childCount / column -1)*column)){
                    bottom = 20;
                }else{
                    bottom = 2;
                }
                canvas.drawLine(cellView.getRight(), cellView.getTop()-20, cellView.getRight(), cellView.getBottom()+bottom,    //竖线
                        localPaint);
                if(childCount % column == 0 && (i+1) > ((childCount / column -1)*column)) continue;
                canvas.drawLine(cellView.getLeft(), cellView.getBottom(), cellView.getRight(), cellView.getBottom(),            //横线
                        localPaint);
            }
        }
      //这段代码作用：空白item也画上线条（比如：一行显示4列，gridview总共只有6个内容要显示，那么第二行的最后两个item就是空白item）
//        if (childCount % column != 0) {
//            for (int j = 0; j < (column - childCount % column); j++) {
//                View lastView = getChildAt(childCount - 1);
//                canvas.drawLine(lastView.getRight() + lastView.getWidth() * j, lastView.getTop(), lastView.getRight()
//                        + lastView.getWidth() * j, lastView.getBottom(), localPaint);
//            }
//        }
    }
}