package com.project.crawlerservice.job.asset.daily.step;

import com.project.crawlerservice.dto.AssetDataDTO;
import com.project.crawlerservice.dto.ExchangeRateDTO;
import com.project.crawlerservice.enums.Currency;
import com.project.crawlerservice.job.asset.daily.dto.DailyAssetChangeProcessorDTO;
import com.project.crawlerservice.job.asset.daily.dto.DailyAssetChangeWriterDTO;
import com.project.crawlerservice.rabbit.data.EmailSendMessage;
import com.project.crawlerservice.service.AssetService;
import com.project.crawlerservice.service.CustomService;
import com.project.crawlerservice.service.ExchangeRateService;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DailyAssetChangeProcessor implements ItemProcessor<DailyAssetChangeProcessorDTO, DailyAssetChangeWriterDTO> {

    @Autowired
    private AssetService assetService;

    @Autowired
    private CustomService customService;

    @Autowired
    private ExchangeRateService exchangeRateService;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    @Override
    public DailyAssetChangeWriterDTO process(DailyAssetChangeProcessorDTO dailyAssetChangeProcessorDTO) {
        DailyAssetChangeWriterDTO dailyAssetChangeWriterDTO = new DailyAssetChangeWriterDTO();
        List<AssetDataDTO> assetDataDTOList = customService.findAssetDataByUsername(dailyAssetChangeProcessorDTO.getUsername());
        EmailSendMessage emailSendMessage = new EmailSendMessage();
        emailSendMessage.setTo(dailyAssetChangeProcessorDTO.getMail());
        emailSendMessage.setTitle(dateFormat.format(new Date()) + "Tarihli Varlık Değişim Raporu");
        emailSendMessage.setContent(generateContent(assetDataDTOList));
        emailSendMessage.setSimple(Boolean.FALSE);
        dailyAssetChangeWriterDTO.setEmailSendMessage(emailSendMessage);
        return dailyAssetChangeWriterDTO;
    }

    private String generateContent(List<AssetDataDTO> changes){
        BigDecimal totalAmount = BigDecimal.ZERO;
        StringBuilder htmlContent = new StringBuilder();


        // HTML Başlangıcı
        htmlContent.append("<!DOCTYPE html>");
        htmlContent.append("<html lang=\"tr\">");
        htmlContent.append("<head>");
        htmlContent.append("<meta charset=\"UTF-8\">");
        htmlContent.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
        htmlContent.append("<title>Günlük Varlık Değişim Raporu</title>");
        htmlContent.append("<style>");
        htmlContent.append("body { font-family: Arial, sans-serif; background-color: #f4f6f9; color: #333; margin: 0; padding: 0; }");
        htmlContent.append(".container { width: 80%; margin: 30px auto; background-color: #ffffff; border-radius: 8px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); padding: 20px; }");
        htmlContent.append(".header { text-align: center; margin-bottom: 20px; }");
        htmlContent.append(".header h3 { color: black; font-size: 28px; margin: 0; }");
        htmlContent.append(".header p { color: black; font-size: 16px; margin-top: 5px; }");
        htmlContent.append(".table-container { margin-top: 30px; }");
        htmlContent.append("table { width: 100%; border-collapse: collapse; margin-bottom: 20px; }");
        htmlContent.append("th, td { padding: 12px; text-align: center; border: 1px solid #ddd; white-space: nowrap; }");
        htmlContent.append("td { color: gray; }");
        htmlContent.append("th { background-color: #007bff; color: white; font-weight: bold; }");
        htmlContent.append(".total-assets { font-size: 22px; font-weight: bold; color: #007bff; text-align: right; }");
        htmlContent.append(".footer { text-align: center; margin-top: 30px; color: #888; }");
        htmlContent.append("</style>");
        htmlContent.append("</head>");
        htmlContent.append("<body>");

        // Başlık kısmı
        htmlContent.append("<div class=\"container\">");
        htmlContent.append("<div class=\"header\">");
        htmlContent.append("<h3>Günlük Varlık Değişim Raporu</h3>");
        htmlContent.append("<p><strong>").append(dateFormat.format(new Date())).append("</strong> tarihli günlük varlık raporunuz aşağıdadır.</p>");
        htmlContent.append("</div>");

        // Tablo
        htmlContent.append("<div class=\"table-container\">");
        htmlContent.append("<table>");
        htmlContent.append("<thead>");
        htmlContent.append("<tr>");
        htmlContent.append("<th>Varlık Türü</th>");
        htmlContent.append("<th>Başlangıç Değeri</th>");
        htmlContent.append("<th>Mevcut Değeri</th>");
        htmlContent.append("<th>Değişim Oranı</th>");
        htmlContent.append("</tr>");
        htmlContent.append("</thead>");
        htmlContent.append("<tbody>");

        // Tablo satırları
        for (AssetDataDTO change : changes) {
            ExchangeRateDTO exchangeRateDTOData = exchangeRateService.findByExchangeRate(change.getDataCurrency()).orElse(new ExchangeRateDTO(Currency.TL,Currency.TL.name(),BigDecimal.ONE,BigDecimal.ONE,new Date()));
            ExchangeRateDTO exchangeRateDTOAsset = exchangeRateService.findByExchangeRate(change.getAssetCurrency()).orElse(new ExchangeRateDTO(Currency.TL,Currency.TL.name(),BigDecimal.ONE,BigDecimal.ONE,new Date()));
            String row = String.format("""
                        <tr>
                            <td style="width: 150px;">%s</td>
                            <td style="text-align: right; width: 100px;">%s</td>
                            <td style="text-align: right; width: 100px;">%s</td>
                            <td style="font-weight: bold; color:%s; text-align: right; width: 25px;">%s</td>
                        </tr>
                    """,
                    change.getName(),
                    change.getPiece().multiply(change.getAverage().multiply(exchangeRateDTOAsset.getBuy())).setScale(2, RoundingMode.HALF_UP) + " " + Currency.TL.name(),
                    change.getPiece().multiply(change.getDailyValue().multiply(exchangeRateDTOData.getBuy())).setScale(2, RoundingMode.HALF_UP) + " " + Currency.TL.name(),
                    change.getAverage().compareTo(change.getDailyValue()) > 0 ? "red" : change.getAverage().compareTo(change.getDailyValue()) == 0 ? "gray" : "green",
                    change.getDailyValue().subtract(change.getAverage()).setScale(1, RoundingMode.HALF_UP) + "%");
            totalAmount = totalAmount.add(change.getPiece().multiply(change.getDailyValue().multiply(exchangeRateDTOData.getBuy())));
            htmlContent.append(row);
        }

        htmlContent.append("</tbody>");
        htmlContent.append("</table>");

        // Toplam Varlık
        htmlContent.append("<div class=\"total-assets\">");
        htmlContent.append("<p>Toplam Varlık: ").append(totalAmount.setScale(2, RoundingMode.HALF_UP)).append(" ").append(Currency.TL.name()).append("</p>");
        htmlContent.append("</div>");

        htmlContent.append("</div>");

        // Alt Kısım
        htmlContent.append("<div class=\"footer\">");
        htmlContent.append("<p style=\"font-size: 13px; color: gray;\">Bu e-posta bilgilendirme amaçlıdır. Günlük olarak otomatik gönderilmektedir.</p>");

        htmlContent.append("</div>");
        htmlContent.append("</div>");

        // HTML Kapanışı
        htmlContent.append("</body>");
        htmlContent.append("</html>");
        return htmlContent.toString();
    }

}