package belykh.projects.TelegramBot.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ReportDto implements Serializable {

    static final long serialVersionUID = 1L;

    private String typeObject;

    private Integer countTimer;

    private String totalTime;

    @Override
    public String toString() {
        return "Блюдо = " + typeObject +
                ", кол-во замеров = " + countTimer +
                ", общее время приготовления = " + totalTime ;
    }
}
