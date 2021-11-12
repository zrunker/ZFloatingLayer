package cc.ibooker.floatinglayer.sample.dto;

import cc.ibooker.floatinglayer.core.combination.CombinationEnum;
import cc.ibooker.floatinglayer.sample.view.ViewType;

/**
 * @program: ZFloatingLayer
 * @description: 发送枚举
 * @author: zoufengli01
 * @create: 2021-10-15 16:27
 **/
public enum SendEnum {
    SEND_RIGHT_TO_LEFT_ONE(CombinationEnum.COMBINATION_ANIMATOR_THREE, ViewType.VIEW_RIGHT_TO_LEFT_1),
    SEND_SHOW_TO_HIDE_ONE(CombinationEnum.COMBINATION_ANIMATOR_FOUR, ViewType.VIEW_SHOW_TO_HIDE_1);

    private final CombinationEnum combinationEnum;

    @ViewType
    private final String viewType;

    SendEnum(CombinationEnum combinationEnum, String viewType) {
        this.combinationEnum = combinationEnum;
        this.viewType = viewType;
    }

    public CombinationEnum getCombinationEnum() {
        return combinationEnum;
    }

    public String getViewType() {
        return viewType;
    }

    public static SendEnum getValue(@SendType String sendType) {
        switch (sendType) {
            case SendType.SEND_TYPE_ONE:
                return SendEnum.SEND_RIGHT_TO_LEFT_ONE;
            case SendType.SEND_TYPE_TWO:
                return SendEnum.SEND_SHOW_TO_HIDE_ONE;
        }
        return null;
    }

}
