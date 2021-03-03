/*******************************************************************************
 * Copyright (c) 2021 Red Hat Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc. - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.ls.core.internal.managers;

import static org.eclipse.jdt.ls.core.internal.ExtensionsExtractor.extractExtensions;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.ls.core.internal.IConstants;

/**
 * This is an orchestrator who is responsible for fetching and searching
 * IBuildSupport objects.
 *
 * @author D.Bushenko
 *
 */
public class BuildSupportManager {
	private static final BuildSupportManager instance = new BuildSupportManager();
	private List<IBuildSupport> lazyLoadedBuildSupportList;

	private BuildSupportManager() {
	}

	public static BuildSupportManager instance() {
		return instance;
	}

	public List<IBuildSupport> obtainBuildSupports() {
		if (lazyLoadedBuildSupportList == null) {
			lazyLoadedBuildSupportList = extractExtensions(IConstants.PLUGIN_ID, "buildSupport");
		}

		return lazyLoadedBuildSupportList;
	}

	public Optional<IBuildSupport> find(IProject project) {
		return find(bs -> bs.applies(project));
	}

	/////////

	private Optional<IBuildSupport> find(Predicate<? super IBuildSupport> predicate) {
		return obtainBuildSupports()
				.stream()
				.filter(predicate)
				.findFirst();
	}
}
