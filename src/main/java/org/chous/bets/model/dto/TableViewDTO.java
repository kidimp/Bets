package org.chous.bets.model.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 *  DTO, представляющий всю таблицу для раундов группового этапа, нокаут-стадии и таблицы, в которой собраны вместе все матчи турнира (шапка + информация).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class TableViewDTO {

    private List<MatchColumnDTO> matchesColumns;
    private List<TableRowDTO> tableRows;
}
