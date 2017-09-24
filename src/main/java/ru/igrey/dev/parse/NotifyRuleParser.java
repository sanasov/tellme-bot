package ru.igrey.dev.parse;

import org.apache.commons.lang3.StringUtils;
import ru.igrey.dev.domain.notifyrule.NotifyRule;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

public class NotifyRuleParser {
    private String rule;

    public NotifyRuleParser(String rule) {
        this.rule = rule.toLowerCase().trim();
    }

    public NotifyRule parse() {
        if (StringUtils.isBlank(rule)) {
            return null;
        }
        List<LocalDate> dates = new ParsedDate(rule).get();
        LocalTime time = new ParsedTime(rule).get();
        if (dates == null && time == null) {
            return null;
        }
        return new NotifyRule(
                dates != null ? dates : Arrays.asList(LocalDate.now()),
                null,
                time != null ? time : LocalTime.of(9, 0),
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
