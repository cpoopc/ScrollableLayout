# [ScrollableLayout](https://github.com/cpoopc/ScrollableLayout)
共同头部+ViewPager+ListView(AdapterView)|RecyclerView|ScrollView    
blog:http://blog.csdn.net/w7822938/article/details/47173047  
安装包放在/demo-apk,可以直接安装看效果

NOTE:  
2015/8/31:  
添加对RecyclerView的支持.  

效果如下:  
<img width="400" height="720" src="https://github.com/cpoopc/ScrollableLayout/blob/master/image/preview.gif" />

## Usage
1.xml
```
<com.cpoopc.scrollablelayoutlib.ScrollableLayout
        android:id="@+id/scrollableLayout"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical">
<-- header -->
  <-- header example -->
        <RelativeLayout
            android:id="@+id/rl_head"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:background="#cdcdcd">
        </RelativeLayout>
  <-- header example -->

<-- content View -->
  <-- content View example -->
        <com.astuetz.PagerSlidingTabStrip
            android:id="@+id/pagerStrip"
            android:layout_width="match_parent"
            android:layout_height="@dimen/tab_height"
            android:layout_alignParentBottom="true" />
        <android.support.v4.view.ViewPager
            android:id="@+id/viewpager"
            android:layout_width="match_parent"
            android:layout_height="match_parent" />
  <-- content View example -->

    </com.cpoopc.scrollablelayoutlib.ScrollableLayout>
  ```
2.xxFragment|xxView|.. implements ScrollableHelper.ScrollableContainer
```
@Override
    public View getScrollableView() {
        return scrollView;
    }
```
3.设置当前container
```
mScrollLayout.getHelper().setCurrentScrollableContainer(scrollableContainer)
```
