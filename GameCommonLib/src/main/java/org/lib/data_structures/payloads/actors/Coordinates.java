package org.lib.data_structures.payloads.actors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.lib.data_structures.payloads.Payload;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Coordinates extends Payload {
    private int x;
    private int y;
}