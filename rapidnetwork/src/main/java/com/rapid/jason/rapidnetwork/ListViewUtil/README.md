# ListViewUtil

��ǩ��ListViewUtil

## ����
> ListViewUtil ��һ��ͨ�õ��б������������Է���������б����跱�ӵ���дviewhold��

## ʹ��˵��
### 1������Adapter�̳�BaseCardsAdapter

### 2���ڳ�ʼ����������listview��id��context��
```java
super(context, R.layout.card_layout);
```

### 3��ʵ��setViewHolder����
```java
@Override
public void setViewHolder(int position, BaseViewHolder baseViewHolder) {
    //ͨ��getview��������ؼ�id��ȡ�����ˡ�
    TextView textView = baseViewHolder.getView(R.id.tv_filename);
}
```