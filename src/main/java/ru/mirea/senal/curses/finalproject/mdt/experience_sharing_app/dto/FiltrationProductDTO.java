package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.constants.ProductType;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.transfer.ProductFiltration;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class FiltrationProductDTO {
    private Double minPrice = 0.0;
    private Double maxPrice = 1000000000.0;
    private Integer minCapacity = 1;
    private Integer maxCapacity = 1000;
    @NotNull(groups = {ProductFiltration.class})
    private List<TagDTO> tags = new ArrayList<>();
    private List<ProductType> productTypes = new ArrayList<ProductType>(Arrays
            .asList(new ProductType[]{ProductType.COURSE, ProductType.LESSON}));

    public String getTagsId() {
        String result = "";
        if (tags.size() == 0) return result;
        result += tags.get(0).getId();
        for (int i = 1; i < tags.size(); i++) {
            result += ", " + tags.get(i).getId();
        }
        return result;
    }

    public String getProductTypes() {
        String result = "";
        if (productTypes.size() == 0) return result;
        result += "'" + productTypes.get(0).toString() + "'";
        for (int i = 1; i < productTypes.size(); i++) {
            result += ", '" + productTypes.get(i).toString() + "'";
        }
        return result;
    }
}
