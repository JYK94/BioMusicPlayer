package com.ljy.musicplayer.biomusicplayer.listview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.dominic.skuface.FaceApi;
import com.ljy.musicplayer.biomusicplayer.BioMusicPlayerApplication;
import com.ljy.musicplayer.biomusicplayer.R;
import com.ljy.musicplayer.biomusicplayer.listview.tab1.ListViewItemFaceEmotion;
import com.ljy.musicplayer.biomusicplayer.listview.tab1.ListViewItemMindwaveEeg;
import com.ljy.musicplayer.biomusicplayer.listview.tab1.ListViewItemMode;
import com.ljy.musicplayer.biomusicplayer.listview.tab1.ListViewItemSong;

import java.util.ArrayList;

/**
 * Created by wjddp on 2018-03-29.
 */

public class ListViewAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {

    private static final int ITEM_VIEW_TYPE_MODE = 0;
    private static final int ITEM_VIEW_TYPE_SONG = 1;
    private static final int ITEM_VIEW_TYPE_MINDWAVE_STATE = 2;
    private static final int ITEM_VIEW_TYPE_NORMAL = 3;
    private static final int ITEM_VIEW_TYPE_MAX = 4;

    // 아이템 데이터 리스트
    private ArrayList<ListViewItem> itemList = new ArrayList<ListViewItem>();

    // 공부모드용 아이템 데이터 리스트
    private ArrayList<ListViewItem> itemListStudy = new ArrayList<ListViewItem>();

    @Override
    public int getItemViewType(int position) {
        return selectItemListByMode().get(position).getType();
    }

    @Override
    public int getViewTypeCount() {
        return ITEM_VIEW_TYPE_MAX;
    }

    @Override
    public int getCount() {
        return selectItemListByMode().size();
    }

    @Override
    public Object getItem(int i) {
        return selectItemListByMode().get(i);
    }

    @Override
    public long getItemId(int i) {
        return selectItemListByMode().get(i).hashCode();
    }

    @Override
    public View getView(int pos, View view, ViewGroup parent) {
        final Context context = parent.getContext();

        Log.d("pwy", "getView called " + pos);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ListViewItem listViewItem = selectItemListByMode().get(pos);
        view = listViewItem.getView(inflater,parent);

        return view;
    }

    //모드별로 아이템 데이터 리스트를 변경해 줌
    private ArrayList<ListViewItem> selectItemListByMode() {
        return BioMusicPlayerApplication.getInstance().isStudyMode() ? itemListStudy : itemList;
    }

    public void addItemMode(String modeName, Boolean modeState, View.OnClickListener toggleButtonEvent) {
        ListViewItemMode item = new ListViewItemMode();

        item.setType(ITEM_VIEW_TYPE_MODE);
        item.setLayoutId(R.layout.listview_item_mode);
        item.setModeName(modeName);
        item.setModeState(modeState);
        item.setOnToggleButtonClick(toggleButtonEvent);

        itemList.add(item);
        itemListStudy.add(item);
    }

    public void addItemSong(Drawable musicImg, String musicName, String singerName, String filePath) {
        ListViewItemSong item = new ListViewItemSong();

        item.setType(ITEM_VIEW_TYPE_SONG);
        item.setLayoutId(R.layout.listview_item_song);
        item.setMusicImg(musicImg);
        item.setMusicName(musicName);
        item.setSingerName(singerName);
        item.setFilePath(filePath);

        ListViewItemSong.songList.add(item);

        itemList.add(item);
        itemListStudy.add(item);
    }

    public void addItemFaceEmotion(Bitmap bitmap, FaceApi.Face face) {
        ListViewItemFaceEmotion item = new ListViewItemFaceEmotion(bitmap, face);

        item.setType(ITEM_VIEW_TYPE_NORMAL);
        item.setLayoutId(R.layout.listview_item_face_emotion);

        if (itemList.get(1) instanceof ListViewItemFaceEmotion) {
            itemList.add(2, item);
            itemList.remove(1);
        } else {
            itemList.add(1, item);
        }
    }

    public ListViewItemMindwaveEeg addItemMindwaveEeg(Activity activity) {
        ListViewItemMindwaveEeg item = new ListViewItemMindwaveEeg(activity);

        item.setType(ITEM_VIEW_TYPE_MINDWAVE_STATE);
        item.setLayoutId(R.layout.listview_item_mindwave_eeg_chart);

        itemListStudy.add(item);
        return item;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ListViewItem item = selectItemListByMode().get(i);

        if (item instanceof ListViewItemSong) {
            BioMusicPlayerApplication.getInstance().getServiceInterface().play(ListViewItemSong.songList.indexOf(item)); // 선택한 오디오재생
            Toast.makeText(view.getContext(), ListViewItemSong.songList.indexOf(item) + " " + ((ListViewItemSong) item).getMusicName(), Toast.LENGTH_SHORT).show();
        }

    }
}
