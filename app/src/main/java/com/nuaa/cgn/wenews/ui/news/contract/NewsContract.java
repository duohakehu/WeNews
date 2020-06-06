package com.nuaa.cgn.wenews.ui.news.contract;

import com.nuaa.cgn.wenews.model.entity.Channel;
import com.nuaa.cgn.wenews.ui.base.BaseCotract;

import java.util.List;

/**
 * Created by cuiguonan on 2019/9/19.
 */

public interface NewsContract{

     interface View extends BaseCotract.BaseView{
         void loadData(List<Channel> mylist, List<Channel> otherlist);
     }
     interface Presenter extends BaseCotract.BasePresenter<View>{

         /**
          * 初始化频道
          */
         void getChannel();
     }
}
