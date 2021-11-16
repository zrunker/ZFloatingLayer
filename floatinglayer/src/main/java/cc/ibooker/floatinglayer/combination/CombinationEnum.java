package cc.ibooker.floatinglayer.combination;


import cc.ibooker.floatinglayer.event.AnimFunStyle;
import cc.ibooker.floatinglayer.location.LocationStrategy;

/**
 * @program: ZFloatingLayer
 * @description: 组合枚举类
 * @author: zoufengli01
 * @create: 2021-10-14 22:25
 **/
public enum CombinationEnum {
    COMBINATION_RIGHT_TO_LEFT_ONE(AnimFunStyle.RIGHT_TO_LEFT, LocationStrategy.TOPY_X_OFFSET),
    COMBINATION_RIGHT_TO_LEFT_TWO(AnimFunStyle.RIGHT_TO_LEFT, LocationStrategy.TOPY_LINE),
    COMBINATION_RIGHT_TO_LEFT_THREE(AnimFunStyle.RIGHT_TO_LEFT, LocationStrategy.TOPY_RANDOM),

    COMBINATION_SHOW_TO_HIDE_ONE(AnimFunStyle.SHOW_TO_HIDE, LocationStrategy.LOCATION_RANDOM);

    @AnimFunStyle
    private final String animFunStyle;

    @LocationStrategy
    private final int locationStrategy;

    CombinationEnum(String animFunStyle, int locationStrategy) {
        this.animFunStyle = animFunStyle;
        this.locationStrategy = locationStrategy;
    }

    public String getAnimFunStyle() {
        return animFunStyle;
    }

    public int getLocationStrategy() {
        return locationStrategy;
    }

    @Override
    public String toString() {
        return "CombinationEnum{" +
                "animFunStyle='" + animFunStyle + '\'' +
                ", locationStrategy=" + locationStrategy +
                '}';
    }
}
