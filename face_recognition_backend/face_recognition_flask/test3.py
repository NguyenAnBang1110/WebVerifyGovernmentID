import face_recognition
import cv2
import numpy as np
import dlib
from PIL import Image



def main():
    print(dlib.__version__)
    image = Image.open("C:/Users/Admin/Downloads/IMG_4438.JPG")
    image.save("C:/Users/Admin/Downloads/IMG_4438_no_exif.JPG")
    image = face_recognition.load_image_file("C:/Users/Admin/Downloads/IMG_4438_no_exif.JPG")
    rgb_image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
    test_image_cv2 = cv2.imread("C:/Users/Admin/Downloads/IMG_4438_no_exif.JPG")
    print(rgb_image.dtype)
    face_locations = face_recognition.face_locations(test_image_cv2)


if __name__ == "__main__":
    main()


def is_safe(row, col, n, rows, cols, diag1, diag2):
    if rows[row] or cols[col] or diag1[row - col + n - 1] or diag2[row + col]:
        return False
    return True


def solve_queens(k, n, board, row, current_sum, rows, cols, diag1, diag2, max_sum):
    if k == 0:
        max_sum[0] = max(max_sum[0], current_sum)
        return

    for r in range(row, n):
        for col in range(n):
            if is_safe(r, col, n, rows, cols, diag1, diag2):
                rows[r] = cols[col] = diag1[r - col + n - 1] = diag2[r + col] = True

                solve_queens(k - 1, n, board, r + 1, current_sum + board[r][col], rows, cols, diag1, diag2, max_sum)

                rows[r] = cols[col] = diag1[r - col + n - 1] = diag2[r + col] = False


def find_max_weight(n, k, board):
    rows = [False] * n
    cols = [False] * n
    diag1 = [False] * (2 * n - 1)
    diag2 = [False] * (2 * n - 1)

    max_sum = [0]

    solve_queens(k, n, board, 0, 0, rows, cols, diag1, diag2, max_sum)

    return max_sum[0]


n, k = map(int, input().split())
board = []
for _ in range(n):
    row = list(map(int, input().split()))
    board.append(row)

max_weight_sum = find_max_weight(n, k, board)

print(max_weight_sum)


def assign_package_to_project(project_index, remaining_budget, package_start_index, num_projects, num_packages, project_budgets, package_values, used_packages, total_ways):
    if remaining_budget == 0:
        process_next_project(project_index + 1, num_projects, num_packages, project_budgets, package_values, used_packages, total_ways)
        return
    if remaining_budget < 0:
        return

    for i in range(package_start_index, num_packages):
        if not used_packages[i]:
            used_packages[i] = True
            assign_package_to_project(project_index, remaining_budget - package_values[i], i + 1, num_projects, num_packages, project_budgets, package_values, used_packages, total_ways)
            used_packages[i] = False

def process_next_project(project_index, num_projects, num_packages, project_budgets, package_values, used_packages, total_ways):
    if project_index == num_projects:
        total_ways[0] += 1
        return

    assign_package_to_project(project_index, project_budgets[project_index], 0, num_projects, num_packages, project_budgets, package_values, used_packages, total_ways)

def calculate_distribution_ways(num_projects, num_packages, project_budgets, package_values):
    used_packages = [False] * num_packages
    total_ways = [0]
    process_next_project(0, num_projects, num_packages, project_budgets, package_values, used_packages, total_ways)
    return total_ways[0]


# Đọc dữ liệu
T = int(input())
for _ in range(T):
    num_projects, num_packages = map(int, input().split())
    project_budgets = list(map(int, input().split()))
    package_values = list(map(int, input().split()))
    result = calculate_distribution_ways(num_projects, num_packages, project_budgets, package_values)
    print(result)




def move_disks(num_disks, start_peg, end_peg, middle_peg, move_steps):
    if num_disks == 1:
        move_steps.append(f"{start_peg}{end_peg}")
        return
    move_disks(num_disks - 1, start_peg, middle_peg, end_peg, move_steps)
    move_steps.append(f"{start_peg}{end_peg}")
    move_disks(num_disks - 1, middle_peg, end_peg, start_peg, move_steps)

num_of_disks = int(input())
move_steps = []
move_disks(num_of_disks, 'A', 'C', 'B', move_steps)
print(len(move_steps) * 2)
for step in move_steps:
    print(step)
    print(step)


def hanoi_circle(n, source, target, auxiliary, steps, clockwise):
    if n == 1:
        steps.append(source)
        steps.append(target)
        return
    if clockwise:
        hanoi_circle(n - 1, source, target, auxiliary, steps, not clockwise)
        steps.append(source)
        steps.append(auxiliary)
        hanoi_circle(n - 1, target, source, auxiliary, steps, not clockwise)
    else:
        hanoi_circle(n - 1, source, auxiliary, target, steps, not clockwise)
        steps.append(source)
        steps.append(target)
        hanoi_circle(n - 1, auxiliary, target, source, steps, not clockwise)

n = int(input())
steps = []

# Nếu n là lẻ, chúng ta di chuyển theo chiều kim đồng hồ
# Nếu n là chẵn, chúng ta di chuyển ngược chiều kim đồng hồ
hanoi_circle(n, 'A', 'C', 'B', steps, n % 2 == 1)

# In kết quả
print("".join(steps))
