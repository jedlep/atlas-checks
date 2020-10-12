package org.openstreetmap.atlas.checks.validation.tag;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.openstreetmap.atlas.checks.base.BaseCheck;
import org.openstreetmap.atlas.checks.flag.CheckFlag;
import org.openstreetmap.atlas.geography.atlas.items.AtlasObject;
import org.openstreetmap.atlas.geography.atlas.items.Relation;
import org.openstreetmap.atlas.tags.AdministrativeLevelTag;
import org.openstreetmap.atlas.tags.BoundaryTag;
import org.openstreetmap.atlas.tags.RelationTypeTag;
import org.openstreetmap.atlas.tags.annotations.validation.Validators;
import org.openstreetmap.atlas.tags.names.NameTag;
import org.openstreetmap.atlas.utilities.configuration.Configuration;


/**
 * @author jedlep
 */

public class BoundaryTagCheck extends BaseCheck<Long>
{

    private static final String MISSING_NAME_INSTRUCTION =
            "Boundary relation {0,number,#} is missing `name` tag.";
    private static final String EMPTY_NAME_INSTRUCTION =
            "Boundary relation {0,number,#} is missing value for `name` tag.";
    private static final String MISSING_ADMIN_LEVEL_INSTRUCTION =
            "Administrative boundary relation {0,number,#} is missing `admin_level` tag.";
    private static final String EMPTY_ADMIN_LEVEL_INSTRUCTION =
            "Boundary relation{0,number,#} has empty value for `admin_level` tag (`admin_level` = {1}).";
    private static final String NON_NUMERIC_ADMIN_LEVEL_INSTRUCTION =
            "Boundary relation {0,number,#} has invalid value for `admin_level` tag (`admin_level` = {1})."
                    + " Should be a valid integer between {2,number,#} and {3,number,#}";
    private static final String ADMIN_LEVEL_OUTSIDE_RANGE_INSTRUCTION =
            "Boundary relation {0,number,#} has invalid value for `admin_level` tag (`admin_level` = {1})."
                    + " Should be between {2,number,#} and {3,number,#}.";

    private static final List<String> FALLBACK_INSTRUCTIONS = List.of(
            MISSING_NAME_INSTRUCTION, EMPTY_NAME_INSTRUCTION,
            MISSING_ADMIN_LEVEL_INSTRUCTION, EMPTY_ADMIN_LEVEL_INSTRUCTION,
            NON_NUMERIC_ADMIN_LEVEL_INSTRUCTION, ADMIN_LEVEL_OUTSIDE_RANGE_INSTRUCTION);

    private static final int RADIX = 10;

    /**
     * Default constructor
     *
     * @param configuration {@link Configuration} required to construct any Check
     */
    public BoundaryTagCheck(final Configuration configuration)
    {
        super(configuration);
    }

    @Override
    public boolean validCheckForObject(final AtlasObject object)
    {
        return object instanceof Relation &&
                Validators.isOfType(object, RelationTypeTag.class, RelationTypeTag.BOUNDARY) &&
                BoundaryTag.isAdministrative(object);
    }

    @Override
    protected Optional<CheckFlag> flag(final AtlasObject object)
    {
        final List<String> instructions = Stream
                .of(this.checkName(object), this.checkAdminLevel(object))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        return instructions.isEmpty() ? Optional.empty() :
                Optional.of(new CheckFlag(this.getTaskIdentifier(object),
                        Collections.singleton(object), instructions));
    }

    @Override
    protected List<String> getFallbackInstructions()
    {
        return FALLBACK_INSTRUCTIONS;
    }

    private Optional<String> checkAdminLevel(final AtlasObject boundary)
    {
        String instruction = null;
        final Optional<String> adminLevel = boundary.getTag(AdministrativeLevelTag.KEY);

        if (adminLevel.isEmpty())
        {
            instruction = this.getLocalizedInstruction(
                    FALLBACK_INSTRUCTIONS.indexOf(MISSING_ADMIN_LEVEL_INSTRUCTION),
                    boundary.getOsmIdentifier());
        }
        else if (adminLevel.get().isBlank())
        {
            instruction = this.getLocalizedInstruction(
                    FALLBACK_INSTRUCTIONS.indexOf(EMPTY_ADMIN_LEVEL_INSTRUCTION),
                    boundary.getOsmIdentifier(),
                    adminLevel.get());
        }
        else
        {
            if (this.isValidInteger(adminLevel.get()))
            {
                final int adminLevelValue;
                if (adminLevel.get().length() > 2 ||
                        (adminLevelValue = Integer.parseInt(adminLevel.get())) >
                                AdministrativeLevelTag.maximumAdministrativeLevelValue() ||
                        adminLevelValue < AdministrativeLevelTag.minimumAdministrativeLevelValue())
                {
                    instruction = this.getLocalizedInstruction(
                            FALLBACK_INSTRUCTIONS.indexOf(ADMIN_LEVEL_OUTSIDE_RANGE_INSTRUCTION),
                            boundary.getOsmIdentifier(),
                            adminLevel.get(),
                            AdministrativeLevelTag.minimumAdministrativeLevelValue(),
                            AdministrativeLevelTag.maximumAdministrativeLevelValue());
                }
            }
            else
            {
                instruction = this.getLocalizedInstruction(
                        FALLBACK_INSTRUCTIONS.indexOf(NON_NUMERIC_ADMIN_LEVEL_INSTRUCTION),
                        boundary.getOsmIdentifier(),
                        adminLevel.get(),
                        AdministrativeLevelTag.minimumAdministrativeLevelValue(),
                        AdministrativeLevelTag.maximumAdministrativeLevelValue());
            }
        }

        return Optional.ofNullable(instruction);
    }

    private Optional<String> checkName(final AtlasObject boundary)
    {
        String instruction = null;

        final Optional<String> name = boundary.getTag(NameTag.KEY);
        if (name.isEmpty())
        {
            instruction = this.getLocalizedInstruction(
                    FALLBACK_INSTRUCTIONS.indexOf(MISSING_NAME_INSTRUCTION),
                    boundary.getOsmIdentifier());
        }
        else if (name.get().isBlank())
        {
            instruction = this.getLocalizedInstruction(
                    FALLBACK_INSTRUCTIONS.indexOf(EMPTY_NAME_INSTRUCTION),
                    boundary.getOsmIdentifier());
        }
        return Optional.ofNullable(instruction);
    }

    private boolean isValidInteger(final String value)
    {
        final char firstCharacter;
        if (value.isEmpty() ||
                Character.digit(firstCharacter = value.charAt(0), RADIX) < 0 &&
                !(value.length() > 1 && firstCharacter == '-'))
        {
            return false;
        }

        for (int i = 1; i < value.length(); i++)
        {
            if (Character.digit(value.charAt(i), RADIX) < 0)
            {
                return false;
            }
        }

        return true;
    }
}
