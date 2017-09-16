package ru.igrey.dev.parse;

import org.apache.commons.lang3.StringUtils;
import ru.igrey.dev.domain.notifyrule.NotifyRule;

public class NotifyRuleParser {
    private String rule;

    public NotifyRuleParser(String rule) {
        this.rule = rule.toLowerCase().trim();
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
        System.out.println(new NotifyRuleParser("tomorrow night").parse());
        System.out.println(new NotifyRuleParser("завтра утром").parse());
        System.out.println(new NotifyRuleParser("днем 18.05").parse());
        System.out.println(new NotifyRuleParser("вечером 28.02").parse());
        System.out.println(new NotifyRuleParser("ночью").parse());
        System.out.println(new NotifyRuleParser("перед сном завтра").parse());
    }

}
