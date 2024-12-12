package com.pakenanya.mindsync.ui.screen.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pakenanya.mindsync.R
import com.pakenanya.mindsync.ui.theme.MindsyncTheme
import kotlinx.coroutines.launch

data class OnboardingPage(
    val image: Int,
    val title: String,
    val description: String
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(onFinished: () -> Unit) {
    val pages = listOf(
        OnboardingPage(
            image = R.drawable.slide_1,
            title = stringResource(R.string.onboarding_page1_title),
            description = stringResource(R.string.onboarding_page1_description)
        ),
        OnboardingPage(
            image = R.drawable.slide_2,
            title = stringResource(R.string.onboarding_page2_title),
            description = stringResource(R.string.onboarding_page2_description)
        ),
        OnboardingPage(
            image = R.drawable.slide_3,
            title = stringResource(R.string.onboarding_page3_title),
            description = stringResource(R.string.onboarding_page3_description)
        )
    )

    val pagerState = rememberPagerState(pageCount = { pages.size })
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { page ->
                OnboardingPage(pages[page])
            }

            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .padding(bottom = 32.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(pages.size) { iteration ->
                        val color = if (pagerState.currentPage == iteration) {
                            Color(0xFF382ACC)
                        } else {
                            Color(0xFFC7C5DE)
                        }
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .weight(1f)
                                .height(4.dp)
                                .clip(RoundedCornerShape(50.dp))
                                .background(color)
                        )
                    }
                }

                if (pagerState.currentPage < pages.size - 1) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            },
                            modifier = Modifier
                                .height(49.dp)
                                .weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF15104D)
                            ),
                            shape = RoundedCornerShape(100.dp)
                        ) {
                            Text(
                                stringResource(R.string.onboarding_next),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                onFinished()
                            },
                            modifier = Modifier
                                .height(49.dp)
                                .weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFE1E0F1)
                            ),
                            shape = RoundedCornerShape(100.dp)
                        ) {
                            Text(
                                stringResource(R.string.onboarding_skip),
                                color = Color(0xFF15104D),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold

                            )
                        }
                    }
                } else {
                    Button(
                        onClick = {
                            onFinished()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(49.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF15104D)
                        ),
                        shape = RoundedCornerShape(100.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.onboarding_get_started),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun OnboardingPage(
    page: OnboardingPage
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Image(
            painter = painterResource(page.image),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.7f)
                .padding(top = 20.dp),
            contentScale = ContentScale.Crop
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(48.dp))
                Text(
                    stringResource(R.string.welcome_to_mindsync),
                    color = Color.Black,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    page.title,
                    color = Color.Black,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    page.description,
                    color = Color.Black,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Light
                )
            }
        }
    }
}

@Preview
@Composable
fun OnboardingScreenPreview() {
    MindsyncTheme {
        OnboardingScreen {}
    }
}