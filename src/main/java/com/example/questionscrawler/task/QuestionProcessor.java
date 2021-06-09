package com.example.questionscrawler.task;


import com.example.questionscrawler.bean.QuestionItem;
import com.example.questionscrawler.service.QuestionService;
import com.example.questionscrawler.utils.HttpUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class QuestionProcessor {

    @Autowired
    private QuestionService questionService;

    private String url = "https://tiku.21cnjy.com/tiku.php?mod=quest&channel=3&cid=0&type=1&xd=2&page=";

    private static int pageNo = 1;

    private static int total = 1;

     @Scheduled(fixedDelay = 1000)
     public void processQuestion(){
         if(pageNo>total) return;
         String html = HttpUtils.doGetHtml(url+pageNo);
         ++pageNo;
         Elements all =  Jsoup.parse(html).select("div.questions_col li");
         //获取题干
         Elements elements = Jsoup.parse(html).select("div.questions_col li");

         total = Integer.parseInt(Jsoup.parse(html).select("div.pg a.last").text().substring(3));

         elements.select("table[name=optionsTable],p").remove();
         List<QuestionItem> questionItemList = new ArrayList<>();
         for(int i=0;i<elements.size();++i){
             Element element = elements.get(i);
             Element allIndex = all.get(i);
             if(allIndex.select("table[name=optionsTable]").size()!=0){
                 String title = element.toString().replace("<li>","").replace("</li>","").replace("&nbsp;","").trim();
                 QuestionItem item = new QuestionItem();
                 item.setQuestionTitle(title);
                 questionItemList.add(item);
             }
         }
         //获取选项
         Elements options = Jsoup.parse(html).select("div.questions_col li table[name=optionsTable] td");
         options.select(".this_jammer,.hidejammersa,.jammerd42").remove();
         for(int i=0;i<options.size()/4;++i){
             QuestionItem item = questionItemList.get(i);
             StringBuilder op = new StringBuilder();
             for(int j=0;j<4;++j){
                 Element element = options.get(i * 4 + j);
                 String s = element.toString();
                 int ix1 = s.indexOf('>');
                 int ix2 = s.lastIndexOf('<');
                 s = s.substring(ix1+1,ix2).replace("&nbsp;","");
                 op.append(s+"\n");
             }
             String optionsAll = "";
             if(!StringUtils.isEmpty(op.toString())){
                 optionsAll = op.toString();
             }
             item.setQuestionOptions(optionsAll);
         }
         //获取答案和考点
         Elements answers = Jsoup.parse(html).select("div.questions_col li p.btns");
         String pre = "https://tiku.21cnjy.com/";
         int cnt = 0;
         for(int i=0;i<answers.size();++i){
             if(all.get(i).select("table[name=optionsTable]").size()==0) continue;
             Element element = answers.get(i);
             String url = pre + element.select("a").attr("href");
             String detailHtml = HttpUtils.doGetHtml(url);
             Document detailDoc = Jsoup.parse(detailHtml);
             //答案
             String op = detailDoc.select("div.answer_detail dd p").first().select("i").text();
             //解析
             String detail = detailDoc.select("div.answer_detail dd p").text();
             //考点
             String point = null;
             point = detail.substring( detail.lastIndexOf("考点")+3).trim();

             QuestionItem item = questionItemList.get(cnt++);
             item.setQuestionUrl(url);
             item.setAnswer(op);
             item.setPoint(point);
         }

        questionService.insertQuestions(questionItemList);
     }
}
