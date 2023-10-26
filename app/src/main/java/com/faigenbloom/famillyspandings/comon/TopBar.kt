@file:OptIn(ExperimentalMaterial3Api::class)

package com.faigenbloom.famillyspandings.comon

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faigenbloom.famillyspandings.R
import com.faigenbloom.famillyspandings.ui.theme.FamillySpandingsTheme

@Composable
fun TopBar(title: String = "") {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = R.drawable.arrow),
                contentDescription = "",
                tint = MaterialTheme.colorScheme.secondary,
            )
        }
        if (title.isEmpty()) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
                    .padding(vertical = 4.dp),
            )
        } else {
            Text(
                modifier = Modifier
                    .padding(vertical = 8.dp),
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary,
            )
        }
    }
}

@Composable
fun StripeBar(
    @StringRes textId: Int,
    secondTabTextId: Int? = null,
    isLeftSelected: Boolean = true,
    onSelectionChanged: ((Boolean) -> Unit)? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Box(
            modifier = Modifier
                .weight(0.5f)
                .clickable { onSelectionChanged?.invoke(true) }
                .background(
                    color = if (isLeftSelected) {
                        MaterialTheme.colorScheme.secondary
                    } else {
                        MaterialTheme.colorScheme.secondaryContainer
                    },
                ),
        ) {
            Text(
                modifier = Modifier
                    .padding(16.dp),
                text = stringResource(id = textId),
                style = MaterialTheme.typography.titleMedium,
                color = if (isLeftSelected) {
                    MaterialTheme.colorScheme.tertiary
                } else {
                    MaterialTheme.colorScheme.onSecondaryContainer
                },
            )
        }
        secondTabTextId?.let {
            Box(
                modifier = Modifier
                    .weight(0.5f)
                    .clickable { onSelectionChanged?.invoke(false) }
                    .background(
                        color = if (isLeftSelected) {
                            MaterialTheme.colorScheme.secondaryContainer
                        } else {
                            MaterialTheme.colorScheme.secondary
                        },
                    ),

            ) {
                Text(
                    modifier = Modifier
                        .padding(16.dp),
                    text = stringResource(id = it),
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isLeftSelected) {
                        MaterialTheme.colorScheme.onSecondaryContainer
                    } else {
                        MaterialTheme.colorScheme.tertiary
                    },
                )
            }
        }
    }
}

@Preview
@Composable
fun TopBarPreview() {
    FamillySpandingsTheme {
        Column(modifier = Modifier.background(Color.White)) {
            TopBar(title = "Auth")
            StripeBar(R.string.authorization)
        }
    }
}

@Preview
@Composable
fun TopBarDoublePreview() {
    FamillySpandingsTheme {
        Column(modifier = Modifier.background(Color.White)) {
            TopBar(title = "Auth")
            StripeBar(
                textId = R.string.authorization,
                secondTabTextId = R.string.registration,
                isLeftSelected = false,
            )
        }
    }
}
