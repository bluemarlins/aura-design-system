package io.github.bluemarlins.aura.foundation

import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.semantics

/**
 * Internal semantics hook naming a component part with its spec Part ID
 * (`aura.component.<name>/<partId>`, see each spec's §2).
 *
 * Spec measurement tests find parts through this key to compare rendered sizes with the
 * spec's Quick Spec block. It is not a test tag, so it never collides with consumer tags,
 * and it adds no accessibility semantics.
 */
internal val AuraPartKey = SemanticsPropertyKey<String>("AuraPart")

internal fun Modifier.auraPart(componentId: String, partId: String): Modifier =
    semantics { this[AuraPartKey] = "$componentId/$partId" }
