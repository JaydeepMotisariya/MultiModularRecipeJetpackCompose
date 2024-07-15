package com.riseup.search.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.riseup.common.navigation.FeatureApi
import com.riseup.common.navigation.NavigationRoute
import com.riseup.common.navigation.NavigationSubGraphRoute


interface SearchFeatureApi : FeatureApi

class SearchFeatureApiImpl : SearchFeatureApi {

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navHostController: NavHostController
    ) {
        navGraphBuilder.navigation(
            route = NavigationSubGraphRoute.Search.route,
            startDestination = NavigationRoute.RecipeList.route
        ) {
            composable(route = NavigationRoute.RecipeList.route) { }
            composable(route = NavigationRoute.RecipeDetails.route) {

            }
        }

    }

}