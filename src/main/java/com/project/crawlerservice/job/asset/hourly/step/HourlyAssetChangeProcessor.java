package com.project.crawlerservice.job.asset.hourly.step;

import com.project.crawlerservice.dto.AssetDataDTO;
import com.project.crawlerservice.dto.ExchangeRateDTO;
import com.project.crawlerservice.enums.Currency;
import com.project.crawlerservice.job.asset.hourly.dto.HourlyAssetChangeProcessorDTO;
import com.project.crawlerservice.job.asset.hourly.dto.HourlyAssetChangeWriterDTO;
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

public class HourlyAssetChangeProcessor implements ItemProcessor<HourlyAssetChangeProcessorDTO, HourlyAssetChangeWriterDTO> {

    @Autowired
    private AssetService assetService;

    @Autowired
    private CustomService customService;

    @Autowired
    private ExchangeRateService exchangeRateService;

    @Override
    public HourlyAssetChangeWriterDTO process(HourlyAssetChangeProcessorDTO hourlyAssetChangeProcessorDTO) {
        HourlyAssetChangeWriterDTO dailyAssetChangeWriterDTO = new HourlyAssetChangeWriterDTO();
        List<AssetDataDTO> assetDataDTOList = customService.findAssetDataHourlyByUsername(hourlyAssetChangeProcessorDTO.getUsername());
        EmailSendMessage emailSendMessage = new EmailSendMessage();
        emailSendMessage.setTo(hourlyAssetChangeProcessorDTO.getMail());
        emailSendMessage.setTitle(new SimpleDateFormat("dd.MM.yyyy HH:mm").format(new Date()) + " Tarihli Varlık Değişim Raporu");
        emailSendMessage.setContent(generateContent(assetDataDTOList));
        emailSendMessage.setSimple(Boolean.FALSE);
        dailyAssetChangeWriterDTO.setEmailSendMessage(emailSendMessage);
        return dailyAssetChangeWriterDTO;
    }

    private String generateContent(List<AssetDataDTO> changes){
        BigDecimal firstTotalAmount = BigDecimal.ZERO;
        BigDecimal lastTotalAmount = BigDecimal.ZERO;
        StringBuilder htmlContent = new StringBuilder();

        // HTML Başlangıcı
        htmlContent.append("<!DOCTYPE html>");
        htmlContent.append("<html lang=\"tr\">");
        htmlContent.append("<head>");
        htmlContent.append("<meta charset=\"UTF-8\">");
        htmlContent.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
        htmlContent.append("<title>Saatlik Varlık Değişim Raporu</title>");
        htmlContent.append("<style>");
        htmlContent.append("body { font-family: Arial, sans-serif; background-color: #f4f6f9; color: #333; margin: 0; padding: 0; }");
        htmlContent.append(".container { width: 80%; margin: 30px auto; background-color: #ffffff; border-radius: 8px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); padding: 20px; }");
        htmlContent.append(".header { text-align: center; margin-bottom: 20px; }");
        htmlContent.append(".header h3 { color: black; font-size: 28px; margin: 0; }");
        htmlContent.append(".header p { color: black; font-size: 16px; margin-top: 5px; }");
        htmlContent.append(".table-container { margin-top: 1px; }");
        htmlContent.append("table { width: 100%; border-collapse: collapse; margin-bottom: 20px; }");
        htmlContent.append("th, td { padding: 12px; text-align: center; border: 1px solid #ddd; white-space: nowrap; }");
        htmlContent.append("td { color: gray; }");
        htmlContent.append("th { background-color: #007bff; color: white; font-weight: bold; }");
        htmlContent.append(".total-assets { font-size: 20px; font-weight: bold; color: #007bff; text-align: left; }");
        htmlContent.append(".footer { text-align: center; margin-top: 30px; color: #888; }");
        htmlContent.append("</style>");
        htmlContent.append("</head>");
        htmlContent.append("<body>");

        // Başlık kısmı
        htmlContent.append("<h3>Saatlik Varlık Değişim Raporu</h3>");
        htmlContent.append("<p><strong>").append(new SimpleDateFormat("dd.MM.yyyy").format(new Date())).append("</strong> tarihli günlük varlık raporunuz aşağıdadır.</p>");
        htmlContent.append("</div>");

        // Tablo
        htmlContent.append("<div class=\"table-container\">");
        htmlContent.append("<table>");
        htmlContent.append("<thead>");
        htmlContent.append("<tr>");
        htmlContent.append("<th>Varlık Türü</th>");
        htmlContent.append("<th>Başlangıç</th>");
        htmlContent.append("<th>Mevcut</th>");
        htmlContent.append("<th>Fark</th>");
        htmlContent.append("<th>Döviz Cinsi</th>");
        htmlContent.append("</tr>");
        htmlContent.append("</thead>");
        htmlContent.append("<tbody>");

        // Tablo satırları
        for (AssetDataDTO change : changes) {
            ExchangeRateDTO exchangeRateDTOData = exchangeRateService.findByExchangeRate(change.getDataCurrency()).orElse(new ExchangeRateDTO(Currency.TL,Currency.TL.name(),BigDecimal.ONE,BigDecimal.ONE,new Date()));
            ExchangeRateDTO exchangeRateDTOAsset = exchangeRateService.findByExchangeRate(change.getAssetCurrency()).orElse(new ExchangeRateDTO(Currency.TL,Currency.TL.name(),BigDecimal.ONE,BigDecimal.ONE,new Date()));
            BigDecimal first = change.getPiece().multiply(change.getAverage().multiply(exchangeRateDTOAsset.getBuy())).setScale(2, RoundingMode.HALF_UP);
            BigDecimal last = change.getPiece().multiply(change.getValue().multiply(exchangeRateDTOData.getBuy())).setScale(2, RoundingMode.HALF_UP);
            firstTotalAmount = firstTotalAmount.add(first);
            lastTotalAmount = lastTotalAmount.add(last);

            String row = String.format("""
                    <tr>
                        <td style="text-align: left;">%s</td>
                        <td style="text-align: right;">%s</td>
                        <td style="text-align: right;">%s</td>
                        <td style="font-weight: bold; color:%s; text-align: right;">%s</td>
                        <td style="text-align: right;">%s</td>
                    </tr>
                """,
                    change.getName(),
                    first,
                    last,
                    first.compareTo(last) > 0 ? "red" : first.compareTo(last) == 0 ? "gray" : "green",
                    last.subtract(first).setScale(2,RoundingMode.HALF_UP),
                    Currency.TL.name());
            htmlContent.append(row);
        }

        String totalRow = String.format("""
                    <tr>
                        <td style="text-align: left;">%s</td>
                        <td style="text-align: right;">%s</td>
                        <td style="text-align: right;">%s</td>
                        <td style="font-weight: bold; color:%s; text-align: right;">%s</td>
                        <td style="text-align: right;">%s</td>
                    </tr>
                """,
                "Toplam",
                firstTotalAmount,
                lastTotalAmount,
                firstTotalAmount.compareTo(lastTotalAmount) > 0 ? "red" : firstTotalAmount.compareTo(lastTotalAmount) == 0 ? "gray" : "green",
                lastTotalAmount.subtract(firstTotalAmount).setScale(2,RoundingMode.HALF_UP),
                Currency.TL.name());
        htmlContent.append(totalRow);

        htmlContent.append("</tbody>");
        htmlContent.append("</table>");

        // Toplam Varlık
        htmlContent.append("<div class=\"total-assets\">");
        htmlContent.append("<p>Toplam Varlık: ").append(lastTotalAmount.setScale(2, RoundingMode.HALF_UP)).append(" ").append(Currency.TL.name()).append("</p>");
        htmlContent.append("</div>");

        htmlContent.append("</div>");

        // Alt Kısım
        htmlContent.append("<div class=\"footer\">");
        htmlContent.append("<p style=\"font-size: 13px; color: gray;\">Bu e-posta bilgilendirme amaçlıdır. Saatlik olarak otomatik gönderilmektedir.</p>");

        // HTML Kapanışı
        htmlContent.append("</body>");
        htmlContent.append("</html>");
        return htmlContent.toString();
    }

    private static String getRow(AssetDataDTO change, ExchangeRateDTO exchangeRateDTOAsset, ExchangeRateDTO exchangeRateDTOData) {
        BigDecimal first = change.getPiece().multiply(change.getAverage().multiply(exchangeRateDTOAsset.getBuy())).setScale(2, RoundingMode.HALF_UP);
        BigDecimal last = change.getPiece().multiply(change.getValue().multiply(exchangeRateDTOData.getBuy())).setScale(2, RoundingMode.HALF_UP);

        return String.format("""
                    <tr>
                        <td style="text-align: left;">%s</td>
                        <td style="text-align: right;">%s</td>
                        <td style="text-align: right;">%s</td>
                        <td style="font-weight: bold; color:%s; text-align: right;">%s</td>
                        <td style="text-align: right;">%s</td>
                    </tr>
                """,
                change.getName(),
                first,
                last,
                first.compareTo(last) > 0 ? "red" : first.compareTo(last) == 0 ? "gray" : "green",
                last.subtract(first).setScale(2,RoundingMode.HALF_UP),
                Currency.TL.name());
    }

}