# MyFilterEdit
一个可过滤字符的edittext

通过在xml布局文件内配置自定义参数进行控制

 filter space_filter:只过滤空格
 
 special_characters_filter：过滤特殊字符（只通过英文大小写 汉子及“·” 通常用于人名及登陆）

<p>一个xml例子</p>

    <com.leo.widget.MyFilterEditText
        android:id="@+id/edit_text"
        android:layout_width="match_parent"
        android:layout_height="40dp"
        app:filter="special_characters_filter|space_filter" />
