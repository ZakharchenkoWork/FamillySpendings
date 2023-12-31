package com.faigenbloom.famillyspandings.budget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faigenbloom.famillyspandings.R
import com.faigenbloom.famillyspandings.comon.SimpleTextField
import com.faigenbloom.famillyspandings.comon.StripeBar
import com.faigenbloom.famillyspandings.comon.TopBar
import com.faigenbloom.famillyspandings.ui.theme.FamillySpandingsTheme

@Composable
fun BudgetPage(
    state: BudgetState,
    onAddSpendingClicked: () -> Unit,
    onAddPlannedSpendingClicked: () -> Unit,
) {
    Column {
        TopBar(title = stringResource(id = R.string.budget_title))
        StripeBar(
            textId = R.string.budget_personal,
            secondTabTextId = R.string.budget_family,
            isLeftSelected = state.isMyBudgetOpened,
            onSelectionChanged = state.onPageChanged,
        )
        Screen(
            state = state,
            onAddSpendingClicked = onAddSpendingClicked,
            onAddPlannedSpendingClicked = onAddPlannedSpendingClicked,
        )
    }
}

@Composable
private fun Screen(
    state: BudgetState,
    onAddSpendingClicked: () -> Unit,
    onAddPlannedSpendingClicked: () -> Unit,
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalAlignment = Alignment.Bottom,
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 40.dp)
                    .weight(1f),
            ) {
                Text(
                    text = stringResource(R.string.budget_balance),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleLarge,
                )
                Text(
                    text = stringResource(R.string.budget_for_month_label),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(CircleShape)
                    .aspectRatio(1f)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                    )
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                SimpleTextField(
                    text = state.familyTotal,
                    label = stringResource(R.string.budget_label_total),
                    onValueChange = state.onTotalChanged,
                    textStyle = MaterialTheme.typography.titleLarge.copy(
                        color = MaterialTheme.colorScheme.onPrimary,
                    ),
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = "UAH",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleLarge,
                )
            }
        }
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            color = MaterialTheme.colorScheme.secondary,
            thickness = 1.dp,
        )

        DataLine(
            label = stringResource(id = R.string.budget_planned_budget),
            amount = state.plannedBudget,
            onValueChanged = state.onPlannedBudgetChanged,
        )
        DataLine(
            label = stringResource(R.string.budget_spent),
            amount = state.spent,
            onPlusClicked = onAddSpendingClicked,
        )
        DataLine(
            label = stringResource(R.string.budget_planned_spendings),
            amount = state.plannedSpendings,
            onPlusClicked = onAddPlannedSpendingClicked,
        )
    }
}

@Composable
private fun DataLine(
    label: String,
    amount: String,
    onValueChanged: ((String) -> Unit)? = null,
    onPlusClicked: (() -> Unit)? = null,
) {
    Text(
        modifier = Modifier
            .padding(top = 16.dp, bottom = 8.dp)
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
            ),
        text = label,
        color = MaterialTheme.colorScheme.onBackground,
        style = MaterialTheme.typography.bodyMedium,
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(color = MaterialTheme.colorScheme.primary),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        onValueChanged?.let {
            SimpleTextField(
                modifier = Modifier
                    .padding(
                        vertical = 4.dp,
                        horizontal = 16.dp,
                    ),
                text = amount,
                label = stringResource(R.string.spending_name),
                onValueChange = onValueChanged,
                textStyle = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.onPrimary,
                ),
            )
        } ?: kotlin.run {
            Text(
                modifier = Modifier
                    .padding(
                        vertical = 4.dp,
                        horizontal = 16.dp,
                    )
                    .weight(1f)
                    .fillMaxWidth(),
                text = amount,
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleLarge,
            )
        }
        onPlusClicked?.let { onClick ->
            Image(
                modifier = Modifier
                    .size(50.dp)
                    .clickable { onClick() },
                painter = painterResource(id = R.drawable.plus),
                contentDescription = "",
            )
        }
    }
}

@Preview
@Composable
fun StatisticsPagePreview() {
    FamillySpandingsTheme {
        Surface {
            BudgetPage(
                state = BudgetState(
                    familyTotal = "4755",
                    isMyBudgetOpened = true,
                    onPageChanged = { },
                    plannedBudget = "20000 UAH",
                    spent = "15245 UAH",
                    plannedSpendings = "3100 UAH",
                    onPlannedBudgetChanged = { },
                    onTotalChanged = { },
                ),
                onAddSpendingClicked = { },
                onAddPlannedSpendingClicked = { },
            )
        }
    }
}
