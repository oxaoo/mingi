package com.github.oxaoo.qas.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 25.03.2017
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataFragment {
    private String link;
    private List<RelevantInfo> relevantInfoList = new ArrayList<>();
}
