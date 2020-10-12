package org.openstreetmap.atlas.checks.validation.tag;

import org.openstreetmap.atlas.geography.atlas.Atlas;
import org.openstreetmap.atlas.utilities.testing.CoreTestRule;
import org.openstreetmap.atlas.utilities.testing.TestAtlas;
import org.openstreetmap.atlas.utilities.testing.TestAtlas.Line;
import org.openstreetmap.atlas.utilities.testing.TestAtlas.Loc;
import org.openstreetmap.atlas.utilities.testing.TestAtlas.Relation;


/**
 * {@link BoundaryTagCheckTest} data generator
 *
 * @author jedlep
 */
public class BoundaryTagCheckTestRule extends CoreTestRule
{
    private static final String WAY_ID = "1";
    private static final String RELATION_ONE = "1";
    private static final String RELATION_TWO = "2";

    @TestAtlas(
            lines = {
                    @Line(id = WAY_ID,
                            coordinates = {@Loc}
                    ),
            },
            relations = {
                    @Relation(id = RELATION_ONE,
                            members = {
                                    @Relation.Member(id = WAY_ID, role = "outer", type = "line")},
                            tags = {
                                "type=boundary",
                                "boundary=administrative"})})
    private Atlas boundaryMissingNameAndAdminLevel;

    @TestAtlas(
            lines = {
                    @Line(id = WAY_ID,
                            coordinates = {@Loc}
                    ),
            },
            relations = {
                    @Relation(id = RELATION_ONE,
                            members = {
                                    @Relation.Member(id = WAY_ID, role = "outer", type = "line")},
                            tags = {
                                "type=boundary",
                                "boundary=administrative",
                                "admin_level=1"})})
    private Atlas boundaryMissingName;

    @TestAtlas(
            lines = {
                    @Line(id = WAY_ID,
                            coordinates = {@Loc}
                    ),
            },
            relations = {
                    @Relation(id = RELATION_ONE,
                            members = {
                                    @Relation.Member(id = WAY_ID, role = "outer", type = "line")},
                            tags = {
                                "type=boundary",
                                "boundary=administrative",
                                "name=",
                                "admin_level=1"}),
                    @Relation(id = RELATION_TWO,
                            members = {
                                    @Relation.Member(id = WAY_ID, role = "outer", type = "line")},
                            tags = {
                                "type=boundary",
                                "boundary=administrative",
                                "name=  ",
                                "admin_level=1"})})
    private Atlas boundaryWithEmptyName;

    @TestAtlas(
            lines = {
                    @Line(id = WAY_ID,
                            coordinates = {@Loc}
                    ),
            },
            relations = {
                    @Relation(id = RELATION_ONE,
                            members = {
                                    @Relation.Member(id = WAY_ID, role = "outer", type = "line")},
                            tags = {
                            "type=boundary",
                            "boundary=administrative",
                            "name=example"})})
    private Atlas boundaryMissingAdminLevel;

    @TestAtlas(
            lines = {
                    @Line(id = WAY_ID,
                            coordinates = {@Loc}
                    ),
            },
            relations = {
                    @Relation(id = RELATION_ONE,
                            members = {
                                @Relation.Member(id = WAY_ID, role = "outer", type = "line")},
                            tags = {
                                "type=boundary",
                                "boundary=administrative",
                                "name=example",
                                "admin_level="}),
                    @Relation(id = RELATION_TWO,
                            members = {
                                @Relation.Member(id = WAY_ID, role = "outer", type = "line")},
                            tags = {
                                "type=boundary",
                                "boundary=administrative",
                                "name=example",
                                "admin_level=  "})})
    private Atlas boundaryWithEmptyAdminLevel;

    @TestAtlas(
            lines = {
                    @Line(id = WAY_ID,
                            coordinates = {@Loc}
                    ),
            },
            relations = {
                    @Relation(id = RELATION_ONE,
                            members = {
                                    @Relation.Member(id = WAY_ID, role = "outer", type = "line")},
                            tags = {
                            "type=boundary",
                            "boundary=administrative",
                            "name=example",
                            "admin_level=1;2"})})
    private Atlas boundaryWithNonNumericAdminLevel;

    @TestAtlas(
            lines = {
                    @Line(id = WAY_ID,
                            coordinates = {@Loc}
                    ),
            },
            relations = {
                    @Relation(id = RELATION_ONE,
                            members = {
                                    @Relation.Member(id = WAY_ID, role = "outer", type = "line")},
                            tags = {
                                "type=boundary",
                                "boundary=administrative",
                                "name=example",
                                "admin_level=0"}),
                    @Relation(id = RELATION_TWO,
                            members = {
                                    @Relation.Member(id = WAY_ID, role = "outer", type = "line")},
                            tags = {
                                "type=boundary",
                                "boundary=administrative",
                                "name=example",
                                "admin_level=-3"})})
    private Atlas boundaryWithAdminLevelTooLow;

    @TestAtlas(
            lines = {
                    @Line(id = WAY_ID,
                            coordinates = {@Loc}
                    ),
            },
            relations = {
                    @Relation(id = RELATION_ONE,
                            members = {
                                    @Relation.Member(id = WAY_ID, role = "outer", type = "line")},
                            tags = {
                            "type=boundary",
                            "boundary=administrative",
                            "name=example",
                            "admin_level=12"})})
    private Atlas boundaryWithAdminLevelTooHigh;

    @TestAtlas(
            lines = {
                    @Line(id = WAY_ID,
                            coordinates = {@Loc}
                            ),
            },
            relations = {
                    @Relation(id = RELATION_ONE,
                            members = {
                                    @Relation.Member(id = WAY_ID, role = "outer", type = "line")},
                            tags = {
                            "type=boundary",
                            "boundary=administrative",
                            "name=example",
                            "admin_level=1"})})
    private Atlas boundaryWithCorrectTags;

    public Atlas boundaryMissingAdminLevel()
    {
        return this.boundaryMissingAdminLevel;
    }

    public Atlas boundaryMissingName()
    {
        return this.boundaryMissingName;
    }

    public Atlas boundaryMissingNameAndAdminLevel()
    {
        return this.boundaryMissingNameAndAdminLevel;
    }

    public Atlas boundaryWithAdminLevelTooHigh()
    {
        return this.boundaryWithAdminLevelTooHigh;
    }

    public Atlas boundaryWithAdminLevelTooLow()
    {
        return this.boundaryWithAdminLevelTooLow;
    }

    public Atlas boundaryWithCorrectTags()
    {
        return this.boundaryWithCorrectTags;
    }

    public Atlas boundaryWithEmptyAdminLevel()
    {
        return this.boundaryWithEmptyAdminLevel;
    }

    public Atlas boundaryWithEmptyName()
    {
        return this.boundaryWithEmptyName;
    }

    public Atlas boundaryWithNonNumericAdminLevel()
    {
        return this.boundaryWithNonNumericAdminLevel;
    }
}
