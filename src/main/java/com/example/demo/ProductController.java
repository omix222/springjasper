package com.example.demo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Controller
public class ProductController {
	@Autowired
    ResourceLoader resource;

    @RequestMapping(value = "/report", method = RequestMethod.GET)
    public String sample( HttpServletResponse response) {
    	  //データ作成
        HashMap<String, Object> params = new HashMap<String, Object>();

        //ヘッダーデータ作成
        params.put("Client_name", "x project");
        params.put("Date_today", "平成30年10月20日");
        SampleProductDao dao = new SampleProductDao();

        List<SampleProductModel> fields = dao.findByAll();
        //データを検索し帳票を出力
        byte[] output  = getReport(params, fields);
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=" + "sample.pdf");
        response.setContentLength(output.length);

        OutputStream os = null;
        try {
            os = response.getOutputStream();
            os.write(output);
            os.flush();

            os.close();
        } catch (IOException e) {
            e.getStackTrace();
        }
        return null;
    }
    /**
     * ジャスパーレポートコンパイル。バイナリファイルを返却する。
     * @param data
     * @param response
     * @return
     */
    private byte[] getReport(HashMap<String, Object> param, List<SampleProductModel> data) {
        InputStream input;
        try {
            //帳票ファイルを取得
            input = new FileInputStream(resource.getResource("classpath:report/jaspersample.jrxml").getFile());
            //リストをフィールドのデータソースに
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data);
            //帳票をコンパイル
            JasperReport jasperReport = JasperCompileManager.compileReport(input);

            JasperPrint jasperPrint;
            //パラメーターとフィールドデータを注入
            jasperPrint = JasperFillManager.fillReport(jasperReport, param, dataSource);
            //帳票をByte形式で出力
            return  JasperExportManager.exportReportToPdf(jasperPrint);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JRException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }
}
