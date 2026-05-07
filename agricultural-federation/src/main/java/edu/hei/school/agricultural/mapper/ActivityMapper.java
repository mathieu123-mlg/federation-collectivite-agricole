package edu.hei.school.agricultural.mapper;

import edu.hei.school.agricultural.entity.Activity;
import edu.hei.school.agricultural.entity.ActivityType;
import edu.hei.school.agricultural.entity.MemberOccupation;
import edu.hei.school.agricultural.entity.MonthlyRecurrenceRule;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ActivityMapper {
    public Activity mapFromResultSet(ResultSet rs) throws SQLException {
        Activity activity = new Activity();
        activity.setId(rs.getString("id"));
        activity.setCollectivityId(rs.getString("collectivity_id"));
        activity.setLabel(rs.getString("label"));

        String type = rs.getString("activity_type");
        if (type != null) {
            activity.setActivityType(ActivityType.valueOf(type));
        }

        if (rs.getDate("executive_date") != null) {
            activity.setExecutiveDate(
                    rs.getDate("executive_date").toLocalDate());
        }

        Integer weekOrdinal = rs.getObject("week_ordinal", Integer.class);
        String dayOfWeek = rs.getString("day_of_week");
        if (weekOrdinal != null && dayOfWeek != null) {
            MonthlyRecurrenceRule rule = new MonthlyRecurrenceRule();
            rule.setWeekOrdinal(weekOrdinal);
            rule.setDayOfWeek(dayOfWeek);
            activity.setRecurrenceRule(rule);
        }

        String occupations = rs.getString("occupations_concerned");
        if (occupations != null && !occupations.isEmpty()) {
            List<MemberOccupation> list = new ArrayList<>();
            for (String occ : occupations.split(",")) {
                list.add(MemberOccupation.valueOf(occ.trim()));
            }
            activity.setMemberOccupationConcerned(list);
        }

        return activity;
    }
}
