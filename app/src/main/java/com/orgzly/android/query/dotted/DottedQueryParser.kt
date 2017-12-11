package com.orgzly.android.query.dotted

import com.orgzly.android.query.*

open class DottedQueryParser : QueryParser() {

    override val groupOpen   = "("
    override val groupClose  = ")"

    override val operatorsAnd = listOf("and")
    override val operatorsOr = listOf("or")

    override val conditions = listOf(
            ConditionMatch("""^(\.)?b\.(.*)""", { matcher ->
                Condition.InBook(unQuote(matcher.group(2)), matcher.group(1) != null)
            }),

            ConditionMatch("""^(\.)?i\.(.*)""", { matcher ->
                Condition.HasState(unQuote(matcher.group(2)), matcher.group(1) != null)
            }),

            ConditionMatch("""^(\.)?it\.(todo|done|none)""", { matcher ->
                val stateType = StateType.valueOf(matcher.group(2).toUpperCase())
                Condition.HasStateType(stateType, matcher.group(1) != null)
            }),

            ConditionMatch("""^(\.)?p\.([a-zA-Z])""", { matcher ->
                Condition.HasPriority(matcher.group(2), matcher.group(1) != null)
            }),

            ConditionMatch("""^(\.)?ps\.([a-zA-Z])""", { matcher ->
                Condition.HasSetPriority(matcher.group(2), matcher.group(1) != null)
            }),

            ConditionMatch("""^(\.)?t\.(.*)""", { matcher ->
                Condition.HasTag(unQuote(matcher.group(2)), matcher.group(1) != null)
            }),

            ConditionMatch("""^tn\.(.*)""", { matcher ->
                Condition.HasOwnTag(unQuote(matcher.group(1)))
            }),

            ConditionMatch("""^s\.(.*)""", { matcher ->
                val interval = QueryInterval.parse(unQuote(matcher.group(1)))
                if (interval != null) Condition.ScheduledInInterval(interval) else null
            }),

            ConditionMatch("""^d\.(.*)""", { matcher ->
                val interval = QueryInterval.parse(unQuote(matcher.group(1)))
                if (interval != null) Condition.DeadlineInInterval(interval) else null
            })
    )

    override val sortOrders = listOf(
            SortOrderMatch("""^(\.)?o\.(scheduled|sched|s)$""", { matcher ->
                SortOrder.ByScheduled(matcher.group(1) != null)
            }),
            SortOrderMatch("""^(\.)?o\.(deadline|dead|d)$""", { matcher ->
                SortOrder.ByDeadline(matcher.group(1) != null)
            }),
            SortOrderMatch("""^(\.)?o\.(priority|prio|pri|p)$""", { matcher ->
                SortOrder.ByPriority(matcher.group(1) != null)
            }),
            SortOrderMatch("""^(\.)?o\.(notebook|book|b)$""", { matcher ->
                SortOrder.ByBook(matcher.group(1) != null)
            })
    )

    override val supportedOptions = listOf(
            OptionMatch("""^ad\.(\d+)$""", { matcher, options ->
                val days = matcher.group(1).toInt()
                if (days > 0) options.copy(agendaDays = days) else null
            })
    )
}
