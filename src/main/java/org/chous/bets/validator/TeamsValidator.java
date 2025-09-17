package org.chous.bets.validator;

import jakarta.validation.ConstraintValidatorContext;
import org.chous.bets.model.dto.TeamDTO;

import java.util.List;

import static org.chous.bets.util.Constants.TEAMS_HAVE_DUPLICATES;

public class TeamsValidator extends DuplicateCodesValidator<UniqueTeams, List<TeamDTO>> {

    @Override
    public boolean isValid(List<TeamDTO> teamDTOs, ConstraintValidatorContext constraintValidatorContext) {
        if (teamDTOs == null) {
            return true;
        }
        List<String> taskMappingCodes = teamDTOs.stream()
                .map(TeamDTO::getName)
                .toList();
        return isValid(taskMappingCodes, TEAMS_HAVE_DUPLICATES, constraintValidatorContext);
    }
}
