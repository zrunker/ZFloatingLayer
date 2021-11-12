package cc.ibooker.floatinglayer.core.combination;

import cc.ibooker.floatinglayer.core.anim.AnimFunStyle;
import cc.ibooker.floatinglayer.core.anim.AnimType;
import cc.ibooker.floatinglayer.core.flayer.view.strategy.LocationStrategy;

/**
 * @program: ZFloatingLayer
 * @description: 组合枚举类
 * @author: zoufengli01
 * @create: 2021-10-14 22:25
 **/
public enum CombinationEnum {
    COMBINATION_ANIMATOR_ONE(AnimFunStyle.RIGHT_TO_LEFT, AnimType.ANIMATOR, LocationStrategy.TOPY_X_OFFSET),
    COMBINATION_ANIMATOR_TWO(AnimFunStyle.RIGHT_TO_LEFT, AnimType.ANIMATOR, LocationStrategy.TOPY_LINE),
    COMBINATION_ANIMATOR_THREE(AnimFunStyle.RIGHT_TO_LEFT, AnimType.ANIMATOR, LocationStrategy.TOPY_RANDOM),
    COMBINATION_ANIMATOR_FOUR(AnimFunStyle.SHOW_TO_HIDE, AnimType.ANIMATOR, LocationStrategy.LOCATION_RANDOM),

    COMBINATION_ANIMATION_ONE(AnimFunStyle.RIGHT_TO_LEFT, AnimType.ANIMATION, LocationStrategy.TOPY_LINE),
    COMBINATION_ANIMATION_TWO(AnimFunStyle.RIGHT_TO_LEFT, AnimType.ANIMATION, LocationStrategy.TOPY_RANDOM),
    COMBINATION_ANIMATION_THREE(AnimFunStyle.SHOW_TO_HIDE, AnimType.ANIMATION, LocationStrategy.LOCATION_RANDOM),

    COMBINATION_SCROLLER_ONE(AnimFunStyle.RIGHT_TO_LEFT, AnimType.SCROLLER, LocationStrategy.TOPY_X_OFFSET),
    COMBINATION_SCROLLER_TWO(AnimFunStyle.RIGHT_TO_LEFT, AnimType.SCROLLER, LocationStrategy.TOPY_LINE),
    COMBINATION_SCROLLER_THREE(AnimFunStyle.RIGHT_TO_LEFT, AnimType.SCROLLER, LocationStrategy.TOPY_RANDOM),

    COMBINATION_SURFACE_ONE(AnimFunStyle.RIGHT_TO_LEFT, AnimType.SURFACE, LocationStrategy.TOPY_LINE),
    COMBINATION_SURFACE_TWO(AnimFunStyle.RIGHT_TO_LEFT, AnimType.SURFACE, LocationStrategy.TOPY_RANDOM);

    @AnimFunStyle
    private final String animFunStyle;

    @AnimType
    private final String animType;

    @LocationStrategy
    private final int locationStrategy;

    CombinationEnum(String animFunStyle, String animType, int locationStrategy) {
        this.animFunStyle = animFunStyle;
        this.animType = animType;
        this.locationStrategy = locationStrategy;
    }

    public String getAnimFunStyle() {
        return animFunStyle;
    }

    public String getAnimType() {
        return animType;
    }

    public int getLocationStrategy() {
        return locationStrategy;
    }

    @Override
    public String toString() {
        return "CombinationEnum{" +
                "animFunStyle='" + animFunStyle + '\'' +
                ", animType='" + animType + '\'' +
                ", locationStrategy=" + locationStrategy +
                '}';
    }
}
