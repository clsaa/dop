package com.clsaa.dop.server.code.model.bo.commit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wsy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatsBo {
    private int additions;
    private int deletions;
}
