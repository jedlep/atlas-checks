package org.openstreetmap.atlas.checks.validation.tag;

import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.openstreetmap.atlas.checks.configuration.ConfigurationResolver;
import org.openstreetmap.atlas.checks.validation.verifier.ConsumerBasedExpectedCheckVerifier;
import org.openstreetmap.atlas.geography.atlas.Atlas;
import org.openstreetmap.atlas.utilities.configuration.Configuration;


/**
 * @author jedlep
 */
public class BoundaryTagCheckTest
{
    @Rule
    public BoundaryTagCheckTestRule setup = new BoundaryTagCheckTestRule();

    @Rule
    public ConsumerBasedExpectedCheckVerifier verifier = new ConsumerBasedExpectedCheckVerifier();

    private final Configuration emptyConfiguration = ConfigurationResolver.emptyConfiguration();

    @Test
    public void shouldFlagForAdminLevelTooHigh()
    {
        this.verify(this.setup.boundaryWithAdminLevelTooHigh(), 1,"Should be between 1 and 11");
    }

    @Test
    public void shouldFlagForAdminLevelTooLow()
    {
        this.verify(this.setup.boundaryWithAdminLevelTooLow(), 2,"Should be between 1 and 11");
    }

    @Test
    public void shouldFlagForEmptyAdminLevel()
    {
        this.verify(this.setup.boundaryWithEmptyAdminLevel(), 2,"has empty value for `admin_level` tag");
    }

    @Test
    public void shouldFlagForEmptyName()
    {
        this.verify(this.setup.boundaryWithEmptyName(), 2,"is missing value for `name` tag");
    }

    @Test
    public void shouldFlagForMissingAdminLevel()
    {
        this.verify(this.setup.boundaryMissingAdminLevel(), 1,"is missing `admin_level` tag");
    }

    @Test
    public void shouldFlagForMissingName()
    {
        this.verify(this.setup.boundaryMissingName(), 1, "is missing `name` tag");
    }

    @Test
    public void shouldFlagForMissingNameAndAdminLevel()
    {
        this.verify(this.setup.boundaryMissingNameAndAdminLevel(), 1, "is missing `name` tag", "is missing `admin_level` tag");
    }

    @Test
    public void shouldFlagForNonNumericAdminLevel()
    {
        this.verify(this.setup.boundaryWithNonNumericAdminLevel(), 1,"Should be a valid integer");
    }

    @Test
    public void shouldNotFlagCorrectlyTaggedBoundary()
    {
        this.verify(this.setup.boundaryWithCorrectTags(), 0);
    }

    private void verify(final Atlas atlas, final int expectedFlagsCount,
            final String... expectedInstructions)
    {
        this.verifier.actual(atlas,
                new BoundaryTagCheck(this.emptyConfiguration));
        this.verifier.verifyExpectedSize(expectedFlagsCount);
        this.verifier.verify(flag -> Assert.assertEquals(expectedInstructions.length,
                flag.getInstructions().split("\n").length));
        this.verifier.verify(flag -> Stream.of(expectedInstructions)
                .map(flag.getInstructions()::contains)
                .forEach(Assert::assertTrue));
    }
}
