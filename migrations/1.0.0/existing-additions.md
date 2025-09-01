# Existing Additions

## Action Bar?! Titles?! Woah!
Along with them now existing within the audience API, action bars and titles have been added to the `OmniClientChat` as first-class citizens now.

## Images were butchered?!
The image API has been completely overhauled, now no longer involving ANY render operations whatsoever, instead simply solely existing as a CPU-side image loading, saving and manipulation API.
That aside, this doesn't mean that you can't use images for rendering anymore! It's just no longer responsible for handling any of that. Instead, we now have middleman conversion
APIs for converting images to textures, and vice versa.

## No more direct key codes!
Input codes have been replaced by a new wrapping API, giving a more consistent experience across platforms, as well as improved readability and usability of the tens of constant key and mouse codes.
Instead, we now have an `OmniInputCode` interface, which stores the inner code as an `Int`, but also exposes the display name associated with the code by the underlying framework, and it's current press state.
This means that there's no need for use of the relevant `isPressed` functions in `OmniKeyboard` and `OmniMouse`, and no need for manually obtaining the display name of a key or mouse button.
You can also now differentiate between keyboard and mouse input codes, as they are now two distinct classes: `OmniKey` and `OmniMouseButton`.

These come with different `OmniKeys` and `OmniMouseButtons` utility objects, which contain all the relevant constants for each platform.

KeyBindings are also now constructed using `OmniInputCode`s rather than raw key codes, and can automatically determine the intended type of default input code based on the provided `OmniInputCode` implementation.
This means that you can now create a key binding with a default key of `OmniKeys.KEY_R` or `OmniMouseButtons.LEFT`, and it will automatically be detected as a keyboard or mouse binding respectively.
