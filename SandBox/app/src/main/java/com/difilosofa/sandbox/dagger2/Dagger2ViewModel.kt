package com.difilosofa.sandbox.dagger2

import javax.inject.Inject

class Dagger2ViewModel @Inject constructor(
    val impl: Dagger2RepositoryImpl
) {
}