package ru.igrey.dev.notifyrule.parse;

import org.apache.commons.lang3.StringUtils;
import ru.igrey.dev.notifyrule.NotifyRule;

public class NotifyRuleParser {
    private String rule;

    public NotifyRuleParser(String rule) {
        this.rule = rule;
    }

    public NotifyRule parse() {
        if (StringUtils.isBlank(rule)) {
            return null;
        }
        return new NotifyRule(
                new ParsedDate(rule).get(),
                null,
                new ParsedTime(rule).get(),
                null,
                false,
                null
        );
    }


    public static void main(String[] args) {
        System.out.println(new NotifyRuleParser("8:15").parse().getTime());
        System.out.println(new NotifyRuleParser("утром").parse().getTime());
        System.out.println(new NotifyRuleParser("днем").parse().getTime());
        System.out.println(new NotifyRuleParser("вечером").parse().getTime());
        System.out.println(new NotifyRuleParser("ночью").parse().getTime());
    }

}
