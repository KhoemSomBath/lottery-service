SELECT
    drawing_id drawingId,
    post_code postCode,
    two_digits twoDigits,
    three_digits threeDigits
FROM PREFIX_drawing_items
WHERE drawing_id = :drawId AND post_code IN ('A', 'B');