package org.lib.data.payloads.actors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.lib.data.dto.ItemDTO;
import org.lib.data.payloads.Payload;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Inventory extends Payload {
    private List<ItemDTO> items;
}
