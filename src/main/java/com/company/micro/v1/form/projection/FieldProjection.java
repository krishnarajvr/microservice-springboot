

package com.company.micro.v1.form.projection;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <h1>Admin Product Projection</h1>
 * Admin Product Projection represents the json structure of Product entity.
 */
@Setter
@Getter
@NoArgsConstructor
public class FieldProjection {

    private String name;

    private String code;

    private String meta;
}
