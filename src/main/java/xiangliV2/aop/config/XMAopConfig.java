package xiangliV2.aop.config;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class XMAopConfig {

    private String pointCut;

    private String aspectClass;

    private String aspectBefore;

    private String aspectAfter;

    private String aspectThorw;

    private String aspectThrowName;
}
