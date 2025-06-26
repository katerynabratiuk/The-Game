package org.lib.data.payloads.actors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.lib.data.payloads.Payload;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Coordinates extends Payload {
    private int x;
    private int y;
}